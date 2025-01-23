package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryReturnRouteDetails {
    UUID id;
    Long parcelId;
    String deliveryStatus;
    String returnToken;
    String updateStatus;
}
