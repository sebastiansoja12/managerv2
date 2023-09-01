package com.warehouse.redirect.infrastructure.adapter.secondary;

import com.warehouse.redirect.domain.port.secondary.RedirectServicePort;
import com.warehouse.shipment.infrastructure.api.ShipmentService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectParcelAdapter implements RedirectServicePort {

    private final ShipmentService service;

    @Override
    public boolean exists(Long parcelId) {
        return service.exists(parcelId);
    }
}
