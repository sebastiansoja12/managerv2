package com.warehouse.mail.infrastructure.adapter.primary.event;

import lombok.Builder;

public class NotificationEvent {
    private NotificationDto notification;

    @Builder
    public NotificationEvent(final NotificationDto notification) {
        this.notification = notification;
    }

    public NotificationDto getNotification() {
        return notification;
    }
}
