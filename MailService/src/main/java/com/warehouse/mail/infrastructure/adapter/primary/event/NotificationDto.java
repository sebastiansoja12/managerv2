package com.warehouse.mail.infrastructure.adapter.primary.event;

public class NotificationDto {
    private String subject;
    private String body;
    private String recipient;

    public NotificationDto(final String subject, final String body, final String recipient) {
        this.subject = subject;
        this.body = body;
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getRecipient() {
        return recipient;
    }
}
