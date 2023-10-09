package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.Status;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SupplyRoute {
    Route route;
    Status status;
}
