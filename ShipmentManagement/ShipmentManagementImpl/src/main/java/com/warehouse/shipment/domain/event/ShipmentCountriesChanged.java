package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentCountriesChanged extends ShipmentChanged implements ShipmentEvent {
    public ShipmentCountriesChanged(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
