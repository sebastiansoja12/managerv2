package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Signature;

public interface SignatureRepository {
    void save(final Signature signature);
    Signature get(final ShipmentId shipmentId);
}
