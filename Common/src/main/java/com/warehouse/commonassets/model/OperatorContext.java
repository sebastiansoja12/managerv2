package com.warehouse.commonassets.model;

import com.warehouse.commonassets.identificator.OperatorId;

public interface OperatorContext {
    OperatorId operatorId();
    void assignOperator(final OperatorId operatorId);
}
