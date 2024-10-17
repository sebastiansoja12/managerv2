package com.warehouse.redirect;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentDto;

public interface RedirectService {
    void redirectShipment(final ShipmentDto shipment);
}
