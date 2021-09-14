package com.warehouse.warehouse.service;
import com.warehouse.warehouse.exceptions.WarehouseMailException;
import com.warehouse.warehouse.model.ParcelNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import com.warehouse.warehouse.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
class MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    void sendNotification(ParcelNotification parcelNotification) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("inparcel@four.com");
            messageHelper.setTo(parcelNotification.getRecipient());
            messageHelper.setSubject(parcelNotification.getSubject());
            messageHelper.setText(parcelNotification.getBody());
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Powiadomienie wysłane");
        } catch (MailException e) {
            log.error("Błąd", e);
            throw new WarehouseMailException("Blad  " + parcelNotification.getRecipient(), e);
        }
    }




}