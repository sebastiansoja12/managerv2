package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import java.util.concurrent.ThreadLocalRandom;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

@Mapper
public interface UserMapper {

    UserEntity map(User user);

    User map(UserEntity userEntity);


    @Mapping(target = "nonExpired", source = "accountNonExpired")
    @Mapping(target = "nonLocked", source = "accountNonLocked")
    UserResponse mapToUserResponse(UserEntity userEntity);

    @AfterMapping
    default void generateIdForUserEntity(@MappingTarget final UserEntity userEntity) {
        if (userEntity.getId() == null) {
            userEntity.setId(generateUniqueId());
        }
    }

    private static Long generateUniqueId() {
        final long timestamp = System.currentTimeMillis();
        final long randomPart = ThreadLocalRandom.current().nextLong(1_000_000L, 10_000_000L);
        return timestamp * 10_000_000L + randomPart;
    }
}
