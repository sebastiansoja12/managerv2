package com.warehouse.redirect;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;

public interface RedirectService {
    void redirectShipment(final ShipmentIdDto shipmentId);
}
