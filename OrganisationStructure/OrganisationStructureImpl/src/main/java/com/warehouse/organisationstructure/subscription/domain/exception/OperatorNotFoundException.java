package com.warehouse.organisationstructure.subscription.domain.exception;

import com.warehouse.commonassets.identificator.OperatorId;

public class OperatorNotFoundException extends RuntimeException {

    public OperatorNotFoundException(final OperatorId operatorId) {
        super("Operator " + operatorId + " was not found");
    }
}
