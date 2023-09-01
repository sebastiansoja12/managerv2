package com.warehouse.delivery.domain.model;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryRouteResponse {
    UUID id;
    String token;
}
