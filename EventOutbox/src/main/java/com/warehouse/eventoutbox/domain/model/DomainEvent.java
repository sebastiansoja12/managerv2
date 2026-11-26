package com.warehouse.eventoutbox.domain.model;

import java.time.Instant;

public interface DomainEvent {

    Instant getTimestamp();
}
