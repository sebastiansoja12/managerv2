package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.SignatureSnapshot;

import java.time.Instant;

public class SignatureSigned extends SignatureChangedEvent implements SignatureEvent {
    public SignatureSigned(final SignatureSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
