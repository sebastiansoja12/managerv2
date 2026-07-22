package com.warehouse.commonassets.kafka.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warehouse.commonassets.identificator.OperatorId;

public interface OperatorAwareEvent {

    @JsonIgnore
    OperatorId operatorId();
}
