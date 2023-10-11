package com.warehouse.redirect.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.redirect.domain.model.ConstantBodyMailMessage;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;
import com.warehouse.redirect.domain.vo.RedirectNotification;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.infrastructure.adapter.secondary.mapper.NotificationMapper;

import com.warehouse.redirect.infrastructure.adapter.secondary.properties.RedirectTokenProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailAdapter implements MailServicePort {

    private final MailPort mailPort;

    private final NotificationMapper mapper;

    private final RedirectTokenProperties properties;

    @Override
    public void sendRedirectInformation(RedirectToken redirectToken) {
        final RedirectNotification redirectNotification = buildNotification(redirectToken);
        final Notification notification = mapper.map(redirectNotification);
        mailPort.sendNotification(notification);
    }

    private RedirectNotification buildNotification(RedirectToken redirectToken) {
        final ConstantBodyMailMessage message = new ConstantBodyMailMessage(redirectToken.getToken());
        return RedirectNotification.builder()
                .body(message.getBody())
                .recipient(redirectToken.getEmail())
                .subject(properties.getSubject())
                .build();
    }
}
