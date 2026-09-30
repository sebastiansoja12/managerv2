package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.Optional;

public interface OperatorContextProvider {

    Optional<OperatorDetails> currentContext();

    default OperatorId currentOperatorId() {
        return currentContext().map(OperatorDetails::operatorId)
                .orElseThrow(() -> new IllegalStateException("Operator context is required"));
    }

    default UserId currentUserId() {
        return currentContext().map(OperatorDetails::userId)
                .orElseThrow(() -> new IllegalStateException("User context is required"));
    }

    default DepartmentId currentDepartmentId() {
        return currentContext().map(OperatorDetails::departmentId)
                .orElseThrow(() -> new IllegalStateException("Department context is required"));
    }
}
