package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentDeliveryPlan;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;
import java.util.Optional;

public class ShipmentCreated extends ShipmentChanged implements ShipmentEvent {

    private final ShipmentSnapshot snapshot;

    private final ShipmentDeliveryPlan deliveryPlan;

    public ShipmentCreated(final ShipmentSnapshot snapshot, final Instant timestamp) {
        this(snapshot, null, timestamp);
    }

    public ShipmentCreated(final ShipmentSnapshot snapshot, final ShipmentDeliveryPlan deliveryPlan,
                           final Instant timestamp) {
        super(snapshot, timestamp);
        this.snapshot = snapshot;
        this.deliveryPlan = deliveryPlan;
    }

    @Override
    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }

    public Optional<ShipmentDeliveryPlan> getDeliveryPlan() {
        return Optional.ofNullable(deliveryPlan);
    }
}
