package com.warehouse.shipment.domain.event;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ShipmentDerivationType;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentDerived implements ShipmentEvent {

    private final ShipmentSnapshot snapshot;

    private final ShipmentId sourceShipmentId;

    private final ShipmentDerivationType derivationType;

    private final Instant timestamp;

    public ShipmentDerived(final ShipmentSnapshot snapshot, final ShipmentId sourceShipmentId,
                           final ShipmentDerivationType derivationType, final Instant timestamp) {
        this.snapshot = snapshot;
        this.sourceShipmentId = sourceShipmentId;
        this.derivationType = derivationType;
        this.timestamp = timestamp;
    }

    @Override
    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }

    public ShipmentId getSourceShipmentId() {
        return sourceShipmentId;
    }

    public ShipmentDerivationType getDerivationType() {
        return derivationType;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
