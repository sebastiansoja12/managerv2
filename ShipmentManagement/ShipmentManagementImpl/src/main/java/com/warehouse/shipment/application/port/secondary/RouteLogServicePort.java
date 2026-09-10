package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.RouteLogRecord;

public interface RouteLogServicePort {

    RouteLogRecord findByShipmentId(final ShipmentId shipmentId);
}
