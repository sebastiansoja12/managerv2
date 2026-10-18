package com.warehouse.pallet.domain.vo;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.OperatorId;

public record UserContext(UserId userId, OperatorId operatorId, DepartmentId departmentId) {

    public UserContext {
        if (userId == null || operatorId == null || departmentId == null) {
            throw new IllegalArgumentException("User context requires user, operator and department");
        }
    }
}
