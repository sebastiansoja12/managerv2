package com.warehouse.terminal.domain.vo;

import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;

public record User(UserId userId, DepartmentCode departmentCode, Username username, OperatorId operatorId) {
    public static User from(final UserDto user) {
        if (user == null) {
            return null;
        }
        final OperatorId operatorId = user.operatorId() != null ? new OperatorId(user.operatorId().value()) : null;
        return new User(new UserId(user.userId().value()), new DepartmentCode(user.departmentCode()),
                new Username(user.username()), operatorId);
    }
}
