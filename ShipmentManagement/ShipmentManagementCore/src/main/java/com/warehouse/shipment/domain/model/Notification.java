package com.warehouse.shipment.domain.model;

import lombok.Builder;


@Builder
public final class Notification {
    private final String subject;
    private final String recipient;
    private final String body;

    public Notification(final String subject, final String recipient, final String body) {
        this.subject = subject;
        this.recipient = recipient;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getBody() {
        return body;
    }
}
