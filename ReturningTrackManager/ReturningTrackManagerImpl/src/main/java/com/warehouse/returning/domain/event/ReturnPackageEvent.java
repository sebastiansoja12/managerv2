package com.warehouse.returning.domain.event;

import java.time.Instant;

public interface ReturnPackageEvent {
    Instant getTimestamp();
}
