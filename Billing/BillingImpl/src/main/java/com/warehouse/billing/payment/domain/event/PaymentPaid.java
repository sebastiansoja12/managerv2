package com.warehouse.billing.payment.domain.event;

import com.warehouse.billing.payment.domain.vo.PaymentSnapshot;
import com.warehouse.eventoutbox.domain.model.DomainEvent;

import java.time.Instant;

public class PaymentPaid implements DomainEvent {

    private final PaymentSnapshot snapshot;

    private final Instant timestamp;

    public PaymentPaid(final PaymentSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public PaymentSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
