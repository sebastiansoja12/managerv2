package com.warehouse.mail.infrastructure.adapter.primary.event;

public interface NotificationEventPublisher {
    void send(final NotificationEvent notificationEvent);
}