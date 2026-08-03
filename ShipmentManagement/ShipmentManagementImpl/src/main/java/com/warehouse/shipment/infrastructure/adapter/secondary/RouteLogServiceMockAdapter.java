package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.RouteLogRecord;

public class RouteLogServiceMockAdapter implements RouteLogServicePort {

    @Override
    public RouteLogRecord findByShipmentId(final ShipmentId shipmentId) {
        return null;
    }
}
