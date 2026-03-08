package com.hengyu.lab.system.outcome.domain;

import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
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
  private List<Author> authors = new ArrayList<>();
  private String ossPath;

  public void setOssPath(String ossPath) {
    if (StringUtils.isEmpty(ossPath)) {
      throw new BizException(ResultCode.FILE_PATH_NOT_EMPTY);
    }
    this.ossPath = ossPath;
  }
}
