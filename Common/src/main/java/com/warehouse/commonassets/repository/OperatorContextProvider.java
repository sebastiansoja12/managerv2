package com.warehouse.commonassets.repository;

import java.util.Optional;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public interface OperatorContextProvider {

    Optional<OperatorDetails> currentContext();

    default Optional<OperatorId> currentOperatorId() {
        return currentContext().map(OperatorDetails::operatorId);
    }

    default Optional<UserId> currentUserId() {
        return currentContext().map(OperatorDetails::userId);
    }

    default Optional<DepartmentId> currentDepartmentId() {
        return currentContext().map(OperatorDetails::departmentId);
    }
}
