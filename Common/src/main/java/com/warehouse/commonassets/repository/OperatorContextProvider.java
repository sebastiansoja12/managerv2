package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.OperatorId;

import java.util.Optional;

public interface OperatorContextProvider {

    Optional<OperatorId> currentOperatorId();
}
