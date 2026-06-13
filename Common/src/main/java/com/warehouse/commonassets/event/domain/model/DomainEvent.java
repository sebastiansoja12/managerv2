package com.warehouse.commonassets.event.domain.model;

import java.time.Instant;

public interface DomainEvent {

    Instant getTimestamp();
}
