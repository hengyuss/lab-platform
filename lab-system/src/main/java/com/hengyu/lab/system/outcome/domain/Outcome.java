package com.hengyu.lab.system.outcome.domain;

import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.valobj.Author;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Outcome {

  private Long id;
  private String title;
  private OutcomeType type;
  private OutcomeStatus status;
  @Builder.Default
  private List<Author> authors = new ArrayList<>();
  private String ossPath;

  public void setOssPath(String ossPath) {
    if (StringUtils.isEmpty(ossPath)) {
      throw new BizException(ResultCode.FILE_PATH_NOT_EMPTY);
    }
    this.ossPath = ossPath;
  }

  public void assignCorresponding(List<Integer> authorIds) {
    throw new BizException("该成果不是论文类型， 无法设置通讯作者");
  }

  public void assignAuthors(List<Author> authors) {
    this.authors = authors;
  }

}
