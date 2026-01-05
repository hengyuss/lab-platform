package com.hengyu.lab.system.outcome.infrastructure.convert;

import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface AuthorConverter {

  @Mapping(source = "name", target = "authorName")
  AuthorPO toPO(Author author);
  List<AuthorPO> toPOList(List<Author> authorList);
}
