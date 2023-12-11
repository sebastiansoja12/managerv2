package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RouteLogRecord {

    String username;

    Long parcelId;

    String depotCode;

    String supplierCode;

    ParcelStatus parcelStatus;

}
