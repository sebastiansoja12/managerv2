package com.warehouse.commonassets.event.domain.model;

import com.warehouse.commonassets.kafka.domain.model.OperatorAwareEvent;

import java.time.Instant;

public interface DomainEvent extends OperatorAwareEvent {

    Instant getTimestamp();
}
