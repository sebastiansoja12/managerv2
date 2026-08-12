package com.warehouse.tracking.domain.model;

import java.time.OffsetDateTime;

public record TrackingEvent(OffsetDateTime timestamp,
                            String name,
                            String description,
                            String eventCode,
                            TrackingLocation location) {
}
