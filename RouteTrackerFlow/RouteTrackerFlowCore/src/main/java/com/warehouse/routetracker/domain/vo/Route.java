package com.warehouse.routetracker.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Route {

    String username;

    Long parcelId;

    String depotCode;

    String supplierCode;

}
