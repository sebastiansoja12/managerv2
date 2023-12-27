package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SupplyRoute {
    RouteLogRecord routeLogRecord;
    ParcelStatus parcelStatus;
}
