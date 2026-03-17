package com.hengyu.lab.system.outcome.application.query;

import com.hengyu.lab.framework.domain.BasePageQuery;
import lombok.Data;

@Data
public class OutcomePaperQry extends BasePageQuery {

   private String title;
   private Integer publishYear;

   private String issn;
   private String journalName;
   private String authorName;
   private Integer authorSort;
   private Integer isCorrespondingAuthor;
}
