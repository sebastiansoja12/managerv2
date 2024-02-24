package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Jacksonized
@Builder
public class DeliveryReturnRouteDetailsDto {
    UUID id;
    Long parcelId;
    String deliveryStatus;
    String returnToken;
    String updateStatus;
    String username;
    String depotCode;
}
