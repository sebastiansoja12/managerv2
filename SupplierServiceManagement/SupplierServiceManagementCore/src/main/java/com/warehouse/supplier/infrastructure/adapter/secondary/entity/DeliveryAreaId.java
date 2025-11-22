package com.warehouse.supplier.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record DeliveryAreaId(Long value) {
}
