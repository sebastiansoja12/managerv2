package com.warehouse.platformadministration.domain.exception;

import com.warehouse.commonassets.identificator.OperatorId;

public class OperatorNotFoundException extends RuntimeException {

    public OperatorNotFoundException(final OperatorId operatorId) {
        super("Operator " + operatorId + " does not exist");
    }
}
