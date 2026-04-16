package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api;


import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DeliveryReturnRouteRequest {
    String depotCode;
    String supplierCode;
    String username;
    List<DeliveryReturnRouteDetails> deliveryReturnRouteDetails;
}
