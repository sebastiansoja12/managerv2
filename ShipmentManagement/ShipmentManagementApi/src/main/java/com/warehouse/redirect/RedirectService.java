package com.warehouse.redirect;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;

public interface RedirectService {
    void processRedirecting(final ShipmentIdDto shipmentId);
}
