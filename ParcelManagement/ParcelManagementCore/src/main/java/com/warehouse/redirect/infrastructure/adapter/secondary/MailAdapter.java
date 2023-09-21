package com.warehouse.redirect.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;
import com.warehouse.redirect.domain.vo.RedirectNotification;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.infrastructure.adapter.secondary.mapper.NotificationMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailAdapter implements MailServicePort {

    private final MailPort mailPort;

    private final NotificationMapper mapper;

    @Override
    public void sendRedirectInformation(RedirectToken redirectToken) {
        final RedirectNotification redirectNotification = buildNotification(redirectToken);
        final Notification notification = mapper.map(redirectNotification);
        mailPort.sendNotification(notification);
    }

    // TODO INPL-3106
    private RedirectNotification buildNotification(RedirectToken redirectToken) {
        return RedirectNotification.builder()
                .body(redirectToken.getEmail())
                .recipient(redirectToken.getEmail())
                .subject(redirectToken.getToken())
                .build();
    }
}
