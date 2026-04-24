package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

public interface ProcessLogEvent {
    LocalDateTime getCreatedAt();
}
