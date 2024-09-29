package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.vo.RouteProcess;

public interface RouteLogServicePort {
    RouteProcess initializeRouteProcess(final ParcelId parcelId);
}
