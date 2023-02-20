package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserMapper {

    @Mapping(source = "depot.depotCode", target = "depotCode")
    UserEntity map(User user);

    @Mapping(target = "depot.depotCode", source = "depotCode")
    User map(UserEntity userEntity);

    List<User> mapToUserList(List<UserEntity> userEntities);

    List<UserEntity> mapToEntityList(List<User> users);
}
