package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;

public record ShipmentControlCenterResult(ShipmentResult shipment,
                                          RouteLogRecord routeLog,
                                          ShipmentReturnDetails returnPackage) {
}
