package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.Status;
import com.warehouse.routetracker.domain.vo.Route;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SupplyRoute {
    Route route;
    Status status;
}
