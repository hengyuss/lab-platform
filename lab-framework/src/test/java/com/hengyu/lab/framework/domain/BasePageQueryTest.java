package com.hengyu.lab.framework.domain;

import org.junit.jupiter.api.Test;

class BasePageQueryTest {

  @Test
  void test() {
    BasePageQuery basePageQuery = new BasePageQuery();
    basePageQuery.setPageNo(1);
    basePageQuery.setPageSize(1);
  }
}