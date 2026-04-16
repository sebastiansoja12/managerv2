package com.warehouse.commonassets.kafkapublisher;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface KeyedKafkaMessage {
    @JsonIgnore
    String kafkaKey();
}
