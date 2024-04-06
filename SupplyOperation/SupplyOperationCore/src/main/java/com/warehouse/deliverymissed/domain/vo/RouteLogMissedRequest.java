package com.warehouse.deliverymissed.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RouteLogMissedRequest {
    Long parcelId;
    String supplierCode;
    String depotCode;
}
