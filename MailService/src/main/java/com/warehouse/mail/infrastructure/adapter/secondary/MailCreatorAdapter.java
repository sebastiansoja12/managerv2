package com.warehouse.mail.infrastructure.adapter.secondary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.warehouse.mail.domain.port.secondary.MailPort;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.mail.infrastructure.adapter.secondary.exception.WarehouseMailException;

import lombok.extern.slf4j.Slf4j;

/**
 * Class for managing email sending.
 * Used for parcels and reroutes.
 */
@Slf4j
@Service
@PropertySource("classpath:application.properties")
public class MailCreatorAdapter implements MailPort {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends email message to recipient/sender
     *
     * @param notification
     */
    void sendMail(Notification notification) {
        final MimeMessagePreparator messagePreparator = mimeMessage -> {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("inparcel@inp.com");
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