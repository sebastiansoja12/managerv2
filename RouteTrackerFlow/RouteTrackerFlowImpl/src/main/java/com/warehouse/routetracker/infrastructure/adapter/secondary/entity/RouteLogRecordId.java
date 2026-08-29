package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record RouteLogRecordId(String value) implements Serializable {

    public static RouteLogRecordId generate() {
        return new RouteLogRecordId(UUID.randomUUID().toString());
    }
}
