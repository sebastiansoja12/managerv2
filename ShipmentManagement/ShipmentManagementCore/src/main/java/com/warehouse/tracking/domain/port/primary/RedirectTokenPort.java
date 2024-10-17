package com.warehouse.tracking.domain.port.primary;

import com.warehouse.tracking.domain.model.Shipment;
import com.warehouse.tracking.domain.model.Token;

public interface RedirectTokenPort {
    Token notifyShipmentRedirected(final Shipment shipment);

}
