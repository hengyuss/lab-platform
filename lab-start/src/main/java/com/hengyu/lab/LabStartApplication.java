package com.hengyu.lab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hengyu.**.mapper")
public class LabStartApplication {

  public static void main(String[] args) {
    SpringApplication.run(LabStartApplication.class, args);
  }

}
