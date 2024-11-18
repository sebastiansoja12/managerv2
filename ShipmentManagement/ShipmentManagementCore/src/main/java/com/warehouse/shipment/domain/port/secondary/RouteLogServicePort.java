package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;

public interface RouteLogServicePort {
    RouteProcess initializeRouteProcess(final ShipmentId shipmentId, final SoftwareConfiguration softwareConfiguration);
}
