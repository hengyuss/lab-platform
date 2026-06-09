# 代码审查报告

> 审查时间：2026-06-08  
> 审查范围：lab-platform 全项目  
> 问题总数：25 个

---

## 一、有实际 Bug（影响运行正确性）⚠️

### BUG-1：`assignPartition` 和 `assignFactor` 使用了错误的错误码

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/domain/PaperOutcome.java:51-63`

**问题：** `assignPartition` 和 `assignFactor` 两个方法在参数为 null 时都抛出 `OUTCOME_FUND_IS_NULL`，而 `OutcomeResultCode` 中明明定义了正确的 `OUTCOME_PARTITION_IS_NULL(2002)`，却从未被使用。这是拷贝代码后漏改的 bug。

```java
// 错误写法（当前代码）
public PaperOutcome assignPartition(JournalPartition journalPartition) {
    if (journalPartition == null) {
        throw new BizException(OutcomeResultCode.OUTCOME_FUND_IS_NULL); // ❌ 错误码
    }
}

// 正确写法
public PaperOutcome assignPartition(JournalPartition journalPartition) {
    if (journalPartition == null) {
        throw new BizException(OutcomeResultCode.OUTCOME_PARTITION_IS_NULL); // ✅
    }
}
```

---

### BUG-2：`assignPartition` 接口参数永远是 null

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/api/controller/OutcomeController.java:115-118`

**问题：** `journalPartition` 参数缺少 `@RequestBody` 或 `@RequestParam` 注解，Spring MVC 无法绑定请求数据，该参数永远为 null，接口实际上不可用。

```java
// 错误写法（当前代码）
@PutMapping("paper/partition/{id}")
public R<Void> assignPartition(@PathVariable("id") Long outcomeId,
    JournalPartition journalPartition) { // ❌ 缺少绑定注解

// 正确写法
@PutMapping("paper/partition/{id}")
public R<Void> assignPartition(@PathVariable("id") Long outcomeId,
    @RequestParam JournalPartition journalPartition) { // ✅
```

---

### BUG-3：RabbitMQ 配置了手动 ack，但消费者从不 ack

**位置：** `lab-start/src/main/resources/application.yml:25` + `lab-system/src/main/java/com/hengyu/lab/system/outcome/infrastructure/mq/consumer/MessageConsumer.java`

**问题：** `application.yml` 配置了 `acknowledge-mode: manual`，但 `MessageConsumer.handleMessage` 从不调用 `channel.basicAck()` 或 `channel.basicNack()`。每条消息都不会被确认，broker 会反复重投，导致消息无限循环消费。

```java
// 修复方案：在方法末尾添加 ack
@RabbitListener(queues = RabbitmqPaperConfig.PAPER_META_RESULT_QUEUE)
public void handleMessage(PaperMetaResult result, Channel channel,
    @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
    try {
        // ... 业务逻辑
        channel.basicAck(tag, false); // ✅ 成功时 ack
    } catch (Exception e) {
        log.error("消息处理失败", e);
        channel.basicNack(tag, false, false); // ✅ 失败时 nack，不重入队
    }
}
```

---

### BUG-4：生产密码以明文形式被 git 追踪

**位置：** `lab-start/src/main/resources/application-local.yml`（已被 git 追踪）

**问题：** 该文件包含数据库密码、MinIO 密码等敏感信息，且未被 `.gitignore` 排除，密码已进入 git 历史。

**修复方案：**
1. 将 `application-local.yml` 加入 `.gitignore`
2. 用 `git filter-repo` 或 `BFG Repo-Cleaner` 从历史中清除该文件
3. 立即更换所有已泄露的密码

---

## 二、架构和设计问题（影响可维护性）🏗️

### ARCH-1：应用层直接依赖基础设施层（架构边界被穿透）

**位置（共 3 处）：**
- `FeedbackAppService.java:30-31`：直接注入 `FeedbackMapper` 和 `FeedbackConverter`
- `PaperOutcomeQryService.java:27-28`：直接注入 `PaperOutcomeQryMapper` 和 `AuthorMapper`
- `MessageService.java:31`：`listCurrentTeachers()` 返回 `List<TeacherDblpPidPO>`（PO 泄漏到应用层）

**问题：** 应用层不应该感知 Mapper 的存在。这破坏了 DDD 分层的核心约束——应用层只能依赖域层接口。

**修复思路：** 查询场景可以在 domain 或 application 层定义专用的 QueryRepository 接口，在 infrastructure 层实现。或者将复杂查询封装在 infrastructure 的 ReadModel service 里，但接口要定义在 domain 层。

---

### ARCH-2：`Outcome.assignCorresponding` 违反 LSP（里氏替换原则）

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/domain/Outcome.java:38-41`

**问题：** 基类方法默认抛异常，子类才重写实现。任何持有 `Outcome` 引用的代码都不能安全调用这个方法，违反了"子类可以替换父类"的 LSP 原则。

```java
// 错误写法（当前代码）
public abstract class Outcome {
    public void assignCorresponding(List<Integer> authorIds) {
        throw new BizException("该成果不是论文类型，无法设置通讯作者"); // ❌
    }
}

// 正确写法：该方法不属于基类，只定义在 PaperOutcome 上
// 服务层已经是 PaperOutcome 类型，直接调用即可，无需在基类声明
```

---

### ARCH-3：`updateDetails` 做了 3 次独立的 findById + save（6 次 DB 操作）

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/application/service/PaperOutcomeService.java:93-99`

**问题：** 3 次 `updatePaperOutcome` 调用，每次都是独立的 `findById + save`，共 6 次 DB 操作，只做了 1 次更新的事情。

```java
// 错误写法（当前代码）
public void updateDetails(PaperDetailsDTO detailsDTO) {
    updatePaperOutcome(outcomeId, o -> o.assignFactor(...));    // findById + save
    updatePaperOutcome(outcomeId, o -> o.assignFund(...));      // findById + save
    updatePaperOutcome(outcomeId, o -> o.assignPartition(...)); // findById + save
}

// 正确写法
@Transactional(rollbackFor = Exception.class)
public void updateDetails(PaperDetailsDTO detailsDTO) {
    PaperOutcome outcome = paperOutcomeRepository.findById(detailsDTO.getOutcomeId())
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    outcome.assignFactor(detailsDTO.getFactor());
    outcome.assignFund(detailsDTO.getFund());
    outcome.assignPartition(detailsDTO.getJournalPartition());
    paperOutcomeRepository.save(outcome); // 只保存一次 ✅
}
```

---

### ARCH-4：值对象使用 `@Data`，不满足不可变性

**位置：**
- `lab-system/src/main/java/com/hengyu/lab/system/outcome/domain/valobj/ResponsiblePerson.java`
- `lab-system/src/main/java/com/hengyu/lab/system/outcome/domain/valobj/PaperMetaTask.java`

**问题：** DDD 中值对象应该不可变（final 字段，无 setter）。`@Data` 生成了 setter，使其变为可变对象，不符合值对象语义。

```java
// 错误写法（当前代码）
@Data
public class ResponsiblePerson {
    private String name; // ❌ 有 setter
}

// 正确写法
@Value // Lombok @Value = final 字段 + getter + 全参构造，无 setter
public class ResponsiblePerson {
    String name;
}
```

---

### ARCH-5：`ProjectOutcome` 缺少 `@SuperBuilder` 等注解

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/domain/ProjectOutcome.java`

**问题：** `ProjectOutcome` 只有 `@Data`，缺少 `@SuperBuilder`、`@NoArgsConstructor`、`@AllArgsConstructor`，与 `PaperOutcome` 风格不一致，MapStruct 生成代码时无法使用 builder。

---

### ARCH-6：`saveOutcome` 和 `uploadPaperFile` 缺少 `@Transactional`

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/application/service/PaperOutcomeService.java:31-55`

**问题：** `uploadPaperFile` 先上传 OSS 文件，再写 DB。如果 DB 写失败，OSS 文件已孤儿，产生数据不一致。同时这两个方法没有显式的事务边界声明。

---

## 三、代码质量问题（影响可读性和健壮性）🔧

### QUALITY-1：`DomainUtil` 用反射强设 private 字段

**位置：** `lab-framework/src/main/java/com/hengyu/lab/framework/utils/DomainUtil.java`

**问题：** 注释自己都写了"暴力破解"。字段改名、使用 JPMS 模块化后会静默失败。更稳健的方式是给域实体提供 package-private 的 `setId()`，只对同包 infrastructure 代码开放。

---

### QUALITY-2：`AuthService` 注入了从未使用的 `authConfig`

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/user/application/AuthService.java:35`

**问题：** `AuthenticationConfiguration authConfig` 字段被注入但从未使用，是重构遗留物，应删除。

---

### QUALITY-3：`PermissionService` 使用字段注入，与全项目风格不一致

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/permission/application/PermissionService.java:15,18`

**问题：** 整个项目统一使用 `@RequiredArgsConstructor` 构造注入，只有 `PermissionService` 用 `@Autowired` 字段注入，风格不一致。

---

### QUALITY-4：`UserConverter` 重复标注 `@Mapper` 和 `@Component`

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/user/infrastructure/convert/UserConverter.java`

**问题：** `@Mapper(componentModel = "spring")` 已经让 MapStruct 生成带 `@Component` 的实现类，再手动加 `@Component` 会导致 Spring 容器里有两个 bean，可能引发注入冲突。应删除手动添加的 `@Component`。

---

### QUALITY-5：`PaperOutcomeConditionBuilder.authorMapper` 是 `public final`

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/application/builder/PaperOutcomeConditionBuilder.java:17`

**问题：** 字段应为 `private final`，暴露为 `public` 破坏封装。

---

### QUALITY-6：`Role` 声明了 `serialVersionUID` 但未实现 `Serializable`

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/permission/domain/Role.java:16`

**问题：** 无意义的声明，应删除或补全 `implements Serializable`。

---

### QUALITY-7：`OutcomeStatus` 枚举字段缺少访问修饰符

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/domain/constant/OutcomeStatus.java:12-13`

**问题：** 枚举字段是 package-private，应为 `private final`，与 `JournalPartition` 风格不一致。

---

### QUALITY-8：`Outcome.setOssPath` 使用了废弃 API

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/domain/Outcome.java:32`

**问题：** `StringUtils.isEmpty` 在 Spring 5.3 起已废弃，应改为 `!StringUtils.hasText(ossPath)`。

---

### QUALITY-9：`getOssFileUrl` 日志打印了整个对象而不是 ossPath

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/application/service/PaperOutcomeService.java:60`

```java
// 错误写法
log.info("outcome ossPath {}", outcome);       // ❌ 打印整个对象

// 正确写法
log.info("outcome ossPath {}", outcome.getOssPath()); // ✅
```

---

### QUALITY-10：`MessageConsumer.count` 是 public static 可变字段

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/infrastructure/mq/consumer/MessageConsumer.java:26`

**问题：** 多线程消费时线程不安全，且作为静态字段暴露在外没有意义。应改为方法内局部变量。

```java
// 错误写法
public static int count = 0; // ❌

// 正确写法：改为方法内局部变量
public void handleMessage(...) {
    int count = 0;
    // ...
    log.info("共有 {} 数据入库", count);
}
```

---

### QUALITY-11：生产 CI 使用 `flyway:repair`

**位置：** `Jenkinsfile:94`

**问题：** `flyway:repair flyway:migrate` 会重置已执行迁移文件的 checksum，允许修改已执行的脚本。这在生产环境是危险操作，会掩盖迁移文件被篡改的事实，应只在开发环境使用。

---

### QUALITY-12：ID 参数在多处被声明为 `String` 类型

**位置：**
- `OutcomeController.java:55`：`@PathVariable("id") String id`，内部再 `Long.valueOf(id)`
- `PaperOutcomeService.java:57`：`getOssFileUrl(String id)`，内部再 `Long.valueOf(id)`
- `FeedbackAppService.java:76`：`Long.parseLong(feedbackId)`

**问题：** ID 应在控制器参数处直接声明为 `Long`，让 Spring 做类型转换，不需要在内部手动转换。

---

### QUALITY-13：`RegisterCmd` Swagger 注释有笔误

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/user/application/dto/command/RegisterCmd.java:24`

```java
@Schema(description = "身份选择 (1:学生, 1:老师)") // ❌ 应为 (1:学生, 2:老师)
```

---

### QUALITY-14：`JwtUtils` 使用了已废弃的 `SignatureAlgorithm.HS256`

**位置：** `lab-framework/src/main/java/com/hengyu/lab/framework/utils/JwtUtils.java:29`

**问题：** `SignatureAlgorithm.HS256` 在新版 JJWT (0.11+) 中已废弃。现代 API 应使用 `.signWith(key)` 让库自动推断算法。

---

## 四、测试层问题 🧪

### TEST-1：`PaperOutcomeRepositoryImplTest` 注入了无用的 `WebContentGenerator`

**位置：** `PaperOutcomeRepositoryImplTest.java:54` 和 `ProjectOutcomeRepositoryImplTest.java:52`

**问题：** `@Autowired private WebContentGenerator webContentGenerator` 从未被使用，是调试时误留，应删除。

---

### TEST-2：`FeedbackRepositoryImplTest` 是伪集成测试

**位置：** `lab-system/src/test/java/com/hengyu/lab/system/infrastructure/persistence/repository/FeedbackRepositoryImplTest.java`

**问题：** 用了 `@SpringBootTest` 启动完整上下文，却同时 `@MockBean` 了 Mapper 和 Converter，`@Transactional` 也没有实际意义。测的内容完全可以改成纯单元测试（`@ExtendWith(MockitoExtension.class)`），运行更快、意图更清晰。

---

### TEST-3：`JournalTest` 是空类，`PermissionChecker` 是空接口

**位置：**
- `lab-system/src/test/java/com/hengyu/lab/system/outcome/domain/JournalTest.java`
- `lab-system/src/main/java/com/hengyu/lab/system/user/application/gateway/PermissionChecker.java`

**问题：** 前者是测试占位符忘了填，后者是设计中途放弃的接口，都应删除。

---

### TEST-4：`AuthorMapper.selectByOutcomeId` 是死代码

**位置：** `lab-system/src/main/java/com/hengyu/lab/system/outcome/infrastructure/mapper/AuthorMapper.java:25`

**问题：** 定义了 `selectByOutcomeId` 方法，但全项目没有任何地方调用它（实际使用的是 `selectAllByOutcomeId`），应删除。

---

### TEST-5：`PaperOutcomeRepositoryImplTest.delete` 测试有逻辑缺陷

**位置：** `PaperOutcomeRepositoryImplTest.java:207-212`

**问题：** 删除后验证 `deletedAuthors.size() == 1`，但作者表没有逻辑删除，删除后应该是 0。这个断言本身可能是错误的，需要确认业务意图。

---

## 五、问题汇总

| 类别 | 数量 | 严重程度 |
|---|---|---|
| 实际 Bug | 4 | 🔴 高 |
| 架构设计问题 | 6 | 🟠 中高 |
| 代码质量问题 | 14 | 🟡 中 |
| 测试层问题 | 5 | 🟡 中 |
| **合计** | **25** | |

---

## 六、建议修复顺序

1. **立即修复（影响生产安全）**：BUG-4（密码入库）、BUG-3（MQ ack 缺失）
2. **近期修复（影响接口正确性）**：BUG-1（错误码）、BUG-2（参数绑定）
3. **重构优化（影响架构清晰度）**：ARCH-1（应用层边界）、ARCH-2（LSP）、ARCH-3（多次 DB 操作）
4. **代码清理**：其余 QUALITY 和 TEST 类问题

---

*本报告由 AI 代码审查生成，建议结合实际业务逻辑二次确认。*
