package com.warehouse.deliveryreturn.domain.vo;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryReturnRouteResponse {
    UUID id;
    String token;
}
