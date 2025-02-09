package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.UserEntity;

public record User(UserId userId, Username username) {
    public static User from(final UserEntity userEntity) {
        return new User(userEntity.getUserId(), userEntity.getUsername());
    }
}
