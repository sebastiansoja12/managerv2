package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.model.ShipmentRequest;
import com.warehouse.shipment.domain.model.ShipmentResponse;

public interface ShipmentPort {

    ShipmentResponse ship(ShipmentRequest request);
}
