package com.warehouse.mail.domain.service;

import com.warehouse.mail.domain.port.secondary.MailServicePort;
import com.warehouse.mail.domain.vo.Notification;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final MailServicePort mailServicePort;

    @Override
    public void sendNotification(Notification notification) {
        mailServicePort.sendNotification(notification);
    }
}
