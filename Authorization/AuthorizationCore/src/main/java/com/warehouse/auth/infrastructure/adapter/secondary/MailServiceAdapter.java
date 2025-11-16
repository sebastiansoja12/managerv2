package com.warehouse.auth.infrastructure.adapter.secondary;

import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.auth.domain.port.secondary.MailServicePort;
import com.warehouse.auth.domain.vo.EmailNotification;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailServiceAdapter implements MailServicePort {

    private final ApplicationEventPublisher eventPublisher;

    public MailServiceAdapter(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void sendMail(final EmailNotification emailNotification) {
        log.info("Sending email notification to: {}", emailNotification.email());
        final NotificationEvent event = new NotificationEvent(null);
    }
}
