package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class DeliveryReturnRouteDetailsDto {
    UUID id;
    Long parcelId;
    String deliveryStatus;
    String returnToken;
    String updateStatus;
}
