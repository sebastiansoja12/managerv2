package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.mail.infrastructure.adapter.primary.event.MailTemplateNotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

public class NotificationEventPublisherImpl implements NotificationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventPublisherImpl.class);

    private final ApplicationEventPublisher eventPublisher;

    public NotificationEventPublisherImpl(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void send(final NotificationEvent event) {
        log.info("Publishing event {}", event.getClass().getSimpleName());
        this.eventPublisher.publishEvent(event);
    }

    @Override
    public void send(final MailTemplateNotificationEvent event) {
        log.info("Publishing mail notification {}", event.templateKey());
        this.eventPublisher.publishEvent(event);
    }
}
