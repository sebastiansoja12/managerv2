package com.warehouse.mcp.application.port.secondary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.domain.vo.ShipmentTracking;

public interface ShipmentServicePort {
    ShipmentTracking findShipmentTracking(final TrackingNumber trackingNumber);
}
