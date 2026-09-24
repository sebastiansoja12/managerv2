package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.shipment.domain.vo.RouteLogRecord;

public record ShipmentRouteLogResponseApi(
        ShipmentDto shipment, RouteLogRecord routeLog, ReturnDetailsDto returnPackage) {
}
