package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.mail.application.port.secondary.MailSenderServicePort;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.infrastructure.adapter.secondary.exception.WarehouseMailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.nio.charset.StandardCharsets;

public class SmtpMailServiceAdapter implements MailSenderServicePort {

    private static final Logger log = LoggerFactory.getLogger(SmtpMailServiceAdapter.class);

    private final JavaMailSender javaMailSender;
    private final String fromAddress;
    private final String fromName;

    public SmtpMailServiceAdapter(final JavaMailSender javaMailSender,
                                  final String fromAddress,
                                  final String fromName) {
        this.javaMailSender = javaMailSender;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
    }

    @Override
    public void send(final MailMessage mailMessage) {
        final MimeMessagePreparator preparator = mimeMessage -> {
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            helper.setFrom(this.fromAddress, this.fromName);
            helper.setTo(mailMessage.recipient().value());
            helper.setSubject(mailMessage.subject());
            helper.setText(mailMessage.body(), false);
        };
        try {
            this.javaMailSender.send(preparator);
        } catch (final MailException exception) {
            log.error("E-mail '{}' could not be sent: {}", mailMessage.subject(), exception.getMessage());
            throw new WarehouseMailException("E-mail was not sent because of: " + exception.getMessage(), exception);
        }
    }
}
