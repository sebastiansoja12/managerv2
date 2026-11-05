package com.warehouse.mcp.application.port.secondary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.domain.vo.ShipmentRoute;

public interface DeliveryNetworkServicePort {
    ShipmentRoute findShipmentRoute(final TrackingNumber trackingNumber);
}
