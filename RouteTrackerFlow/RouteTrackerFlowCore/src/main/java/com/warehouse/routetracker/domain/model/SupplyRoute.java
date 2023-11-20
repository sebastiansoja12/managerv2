package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.Status;
import com.warehouse.routetracker.domain.vo.RouteLogRecord;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SupplyRoute {
    RouteLogRecord routeLogRecord;
    Status status;
}
