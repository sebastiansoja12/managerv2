package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
public class RouteRequestDto {
    Long parcelId;
    String username;
    String supplierCode;
    String depotCode;
}
