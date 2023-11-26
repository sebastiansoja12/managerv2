package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.enumeration.Status;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RouteLogRecord {

    String username;

    Long parcelId;

    String depotCode;

    String supplierCode;

    Status parcelStatus;

}
