package com.warehouse.route.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Route {

    Long userId;

    Long parcelId;

    String depotCode;

    Long supplierId;

}
