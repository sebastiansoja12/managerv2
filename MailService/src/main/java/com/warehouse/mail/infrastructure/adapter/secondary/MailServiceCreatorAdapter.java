package com.warehouse.mail.infrastructure.adapter.secondary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.warehouse.mail.domain.port.secondary.MailServicePort;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.mail.infrastructure.adapter.secondary.exception.WarehouseMailException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MailServiceCreatorAdapter implements MailServicePort {

    @Autowired
    private JavaMailSender mailSender;

    void sendMail(Notification notification) {
        final MimeMessagePreparator messagePreparator = mimeMessage -> {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("manager@inp.com");
            messageHelper.setTo(notification.getRecipient());
            messageHelper.setSubject(notification.getSubject());
            messageHelper.setText(notification.getBody());
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            log.error("Error", e);
            throw new WarehouseMailException("E-mail was not sent because of: " + e.getMessage());
        }
    }

    @Override
    public void sendNotification(Notification notification) {
        sendMail(notification);
    }
}