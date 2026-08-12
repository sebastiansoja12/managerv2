package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record RouteLogRecordDetailId(Long value) implements Serializable {

    public static RouteLogRecordDetailId generate() {
        return new RouteLogRecordDetailId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    }
}
