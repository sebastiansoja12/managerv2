package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.shipment.domain.vo.RouteLogRecord;

public record ShipmentControlCenterResponseApi(ShipmentDto shipment, RouteLogRecord routeLog) {
}
