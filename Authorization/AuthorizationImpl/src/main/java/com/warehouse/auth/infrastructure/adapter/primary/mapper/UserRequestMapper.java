package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.model.CreateUserCommand;
import com.warehouse.auth.domain.model.UpdateUserCommand;
import com.warehouse.auth.infrastructure.dto.CreateUserApiRequest;
import com.warehouse.auth.infrastructure.dto.UpdateUserApiRequest;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public abstract class UserRequestMapper {

    public static CreateUserCommand toCommand(final CreateUserApiRequest request) {
        return new CreateUserCommand(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.password(),
                request.email(),
                request.role(),
                new DepartmentCode(request.departmentCode()),
                request.language()
        );
    }

    public static UpdateUserCommand toCommand(final UserId userId, final UpdateUserApiRequest request) {
        return new UpdateUserCommand(
                userId,
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                new DepartmentCode(request.departmentCode()),
                request.language()
        );
    }
}
