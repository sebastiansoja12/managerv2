package com.warehouse.redirect.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.redirect.RedirectService;
import com.warehouse.redirect.domain.port.primary.RedirectTokenPort;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;

public class RedirectServiceAdapter implements RedirectService {

    private final RedirectTokenPort redirectTokenPort;

    public RedirectServiceAdapter(final RedirectTokenPort redirectTokenPort) {
        this.redirectTokenPort = redirectTokenPort;
    }

    @Override
    public void processRedirecting(final ShipmentIdDto id) {
        final ShipmentId shipmentId = new ShipmentId(id.getValue());
        this.redirectTokenPort.invalidateToken(shipmentId);
    }
}
