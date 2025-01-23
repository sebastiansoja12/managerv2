package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;


import java.util.List;

import lombok.Value;

@Value
public class DeliveryReturnRouteRequestDto {
    String username;
    String supplierCode;
    String depotCode;
    List<DeliveryReturnRouteDetailsDto> deliveryReturnRouteDetails;
}
