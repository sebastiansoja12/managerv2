package com.warehouse.shipment.domain.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record DangerousGoodId(Long value) {
}
