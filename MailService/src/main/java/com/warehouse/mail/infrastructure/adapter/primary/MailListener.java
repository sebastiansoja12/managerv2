package com.warehouse.mail.infrastructure.adapter.primary;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;

@Component
public class MailListener {

    private final MailPort mailPort;

    public MailListener(final MailPort mailPort) {
        this.mailPort = mailPort;
    }

    @EventListener
    public void handle(final NotificationEvent event) {
        final Notification notification = Notification.from(event.getNotification());
        mailPort.sendNotification(notification);
    }
}
