package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.ParcelId;
import com.warehouse.shipment.domain.vo.RouteProcess;

public interface RouteLogServicePort {
    RouteProcess initializeRouteProcess(ParcelId parcelId);
}
