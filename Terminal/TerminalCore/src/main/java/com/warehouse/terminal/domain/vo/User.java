package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.UserEntity;

public record User(UserId userId, String username) {
    public static User from(final UserEntity userEntity) {
        return new User(new UserId(userEntity.getId()), userEntity.getUsername());
    }
}
