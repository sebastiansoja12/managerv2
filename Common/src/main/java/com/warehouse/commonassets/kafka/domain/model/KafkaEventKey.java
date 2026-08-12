package com.warehouse.commonassets.kafka.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface KafkaEventKey {

    @JsonIgnore
    String kafkaKey();
}
