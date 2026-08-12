package com.warehouse.routetracker.infrastructure.adapter.primary.api;

import jakarta.persistence.Embeddable;

@Embeddable
public record ShipmentId(Long value) {
}
