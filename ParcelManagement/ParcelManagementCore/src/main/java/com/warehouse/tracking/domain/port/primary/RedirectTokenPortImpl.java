package com.warehouse.tracking.domain.port.primary;

import com.warehouse.tracking.domain.model.Shipment;
import com.warehouse.tracking.domain.model.Token;

public class RedirectTokenPortImpl implements RedirectTokenPort {
    @Override
    public Token notifyShipmentRedirected(final Shipment shipment) {
        return null;
    }
}
