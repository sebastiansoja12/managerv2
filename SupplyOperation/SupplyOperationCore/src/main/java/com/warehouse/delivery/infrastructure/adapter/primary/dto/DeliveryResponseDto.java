package com.warehouse.delivery.infrastructure.adapter.primary.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class DeliveryResponseDto {
    UUID id;
    Long parcelId;
    String deliveryStatus;
    UpdateStatusDto updateStatus;
}
