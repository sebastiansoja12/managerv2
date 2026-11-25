package com.warehouse.billing.invoice.domain.event;

import com.warehouse.billing.invoice.domain.vo.InvoiceSnapshot;
import com.warehouse.eventoutbox.domain.model.DomainEvent;

import java.time.Instant;

public class InvoiceIssued implements DomainEvent {

    private final InvoiceSnapshot snapshot;

    private final Instant timestamp;

    public InvoiceIssued(final InvoiceSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public InvoiceSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
