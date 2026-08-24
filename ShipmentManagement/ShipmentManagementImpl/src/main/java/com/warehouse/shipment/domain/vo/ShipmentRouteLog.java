package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.Shipment;

public record ShipmentRouteLog(Shipment shipment, RouteLogRecord routeLog) {
}
