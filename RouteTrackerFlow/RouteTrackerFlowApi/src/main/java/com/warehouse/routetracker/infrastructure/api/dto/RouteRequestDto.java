package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
public class RouteRequestDto {

    Long parcelId;
    Long userId;
    String supplierCode;
    Long depotId;
}
