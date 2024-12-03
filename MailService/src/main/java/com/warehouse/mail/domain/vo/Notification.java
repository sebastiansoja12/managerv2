package com.warehouse.mail.domain.vo;

import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationDto;
import lombok.*;

@Value
@Builder
public class Notification {
    String subject;
    String recipient;
    String body;

    public static Notification from(final NotificationDto notification) {
        return new Notification(notification.getSubject(), notification.getRecipient(), notification.getBody());
    }
}
