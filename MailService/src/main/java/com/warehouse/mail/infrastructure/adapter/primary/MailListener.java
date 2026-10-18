package com.warehouse.mail.infrastructure.adapter.primary;

import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.vo.EmailAddress;
import com.warehouse.mail.infrastructure.adapter.primary.event.MailTemplateNotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationDto;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.MailLocaleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

import java.util.List;

public class MailListener {

    private static final Logger log = LoggerFactory.getLogger(MailListener.class);

    private final MailPort mailPort;
    private final MailLocaleResolver mailLocaleResolver;

    public MailListener(final MailPort mailPort, final MailLocaleResolver mailLocaleResolver) {
        this.mailPort = mailPort;
        this.mailLocaleResolver = mailLocaleResolver;
    }

    @EventListener
    public void handle(final MailTemplateNotificationEvent event) {
        EmailAddress.parse(event.recipientEmail()).ifPresentOrElse(
                recipient -> this.mailPort.send(List.of(new MailRequest(event.templateKey(),
                        this.mailLocaleResolver.forCountry(event.recipientCountryCode()), recipient,
                        event.variables()))),
                () -> log.warn("Mail notification {} skipped: recipient e-mail address is invalid",
                        event.templateKey()));
    }

    @EventListener
    public void handle(final NotificationEvent event) {
        final NotificationDto notification = event.getNotification();
        EmailAddress.parse(notification.getRecipient()).ifPresentOrElse(
                recipient -> this.mailPort.send(new MailMessage(recipient, notification.getSubject(),
                        notification.getBody())),
                () -> log.warn("Notification '{}' skipped: recipient e-mail address is invalid",
                        notification.getSubject()));
    }
}
