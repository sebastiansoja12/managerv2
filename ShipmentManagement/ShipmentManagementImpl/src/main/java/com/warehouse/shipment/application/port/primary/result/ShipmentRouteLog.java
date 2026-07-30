package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.returning.api.dto.ReturnDetailsDto;

public record ShipmentRouteLog(ShipmentResult shipment,
                               RouteLogRecord routeLog,
                               ReturnDetailsDto returnPackage) {
}
