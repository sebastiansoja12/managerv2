package com.warehouse.logistics.infrastructure.adapter.primary.dto;

import com.warehouse.logistics.domain.enumeration.DeliveryMethod;

public record DeliveryMethodUpdateRequestDto(DeliveryMethod method) {
}
