package com.warehouse.redirect.infrastructure.adapter.primary.api;


import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;

public interface RedirectService {
    void processRedirecting(final ShipmentIdDto shipmentId);
}
