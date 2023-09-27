package com.warehouse.routetracker.infrastructure.adapter.primary.api.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
public class RouteRequestDto {

    List<Long> parcelIdList;
    String username;
    String supplierCode;
    String depotCode;
}
