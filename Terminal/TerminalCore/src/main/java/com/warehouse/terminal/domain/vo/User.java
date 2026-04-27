package com.warehouse.terminal.domain.vo;

import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.UserEntity;

public record User(UserId userId, DepartmentCode departmentCode, Username username) {
    public static User from(final UserEntity userEntity) {
        return new User(userEntity.getUserId(),
                new DepartmentCode(userEntity.getDepotCode()), userEntity.getUsername());
    }
    public static User from(final UserDto user) {
        if (user == null) {
            return null;
        }
        return new User(new UserId(user.userId().value()), new DepartmentCode(user.departmentCode()), new Username(user.username()));
    }
}
