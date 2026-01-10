package com.hengyu.lab.system.outcome.domain.query;

import com.hengyu.lab.framework.domain.BasePageQuery;
import lombok.Data;

@Data
public class OutcomeQry extends BasePageQuery {

   private String title;


   private String issn;
   private String journalName;
}
