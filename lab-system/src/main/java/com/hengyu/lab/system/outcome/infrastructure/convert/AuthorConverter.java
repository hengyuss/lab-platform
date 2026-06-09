package com.hengyu.lab.system.outcome.infrastructure.convert;

import com.hengyu.lab.system.outcome.domain.entity.Author;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AuthorConverter {

  @Mapping(source = "name", target = "authorName")
  AuthorPO toPO(Author author);

  @Mapping(source = "authorName", target = "name")
  Author toEntity(AuthorPO authorPO);

  List<AuthorPO> toPOList(List<Author> authorList);
}
