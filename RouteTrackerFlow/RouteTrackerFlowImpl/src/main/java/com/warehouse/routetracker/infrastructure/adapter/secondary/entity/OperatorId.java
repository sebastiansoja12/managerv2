package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public record OperatorId(Long value) implements Serializable {
}
