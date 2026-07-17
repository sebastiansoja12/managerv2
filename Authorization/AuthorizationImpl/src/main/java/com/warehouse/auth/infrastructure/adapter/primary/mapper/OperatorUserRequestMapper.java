package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.model.CreateOperatorUserCommand;
import com.warehouse.auth.infrastructure.dto.CreateOperatorUserApiRequest;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;

public abstract class OperatorUserRequestMapper {

    public static CreateOperatorUserCommand toCommand(final OperatorId operatorId,
                                                      final CreateOperatorUserApiRequest request) {
        return new CreateOperatorUserCommand(
                operatorId,
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
}
