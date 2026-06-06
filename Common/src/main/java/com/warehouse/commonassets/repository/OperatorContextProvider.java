package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.Optional;

public interface OperatorContextProvider {

    Optional<OperatorId> currentOperatorId();

    Optional<UserId> currentUserId();

    Optional<DepartmentId> currentDepartmentId();
}
