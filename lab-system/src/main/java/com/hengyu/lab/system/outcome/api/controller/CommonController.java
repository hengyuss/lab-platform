package com.hengyu.lab.system.outcome.api.controller;

import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.outcome.domain.constant.JournalPartition;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/common")
@Tag(name = "通用模块")
public class CommonController {
  // 获取论文状态的下拉列表
  @GetMapping("/enums/journalPartition")
  public R<List<Map<String, Object>>> getJournalPartitionOptions() {
    List<Map<String, Object>> options = new ArrayList<>();

    // 遍历你定义的 Java 枚举类 (假设叫 PaperStatusEnum)
    for (JournalPartition partition : JournalPartition.values()) {
      Map<String, Object> map = new HashMap<>();
      map.put("code", partition.getCode()); // 给 v-model 绑定的值
      map.put("msg", partition.getMsg()); // 给用户看的中文
      options.add(map);
    }

    return R.ok(options);
  }

}
