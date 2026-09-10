package com.warehouse.mail.domain.model;

import com.warehouse.mail.domain.exception.MailException;
import com.warehouse.mail.domain.vo.EmailAddress;

public record MailMessage(EmailAddress recipient, String subject, String body) {

    public MailMessage {
        if (recipient == null) {
            throw new MailException("Mail recipient is required");
        }
        if (subject == null || subject.isBlank()) {
            throw new MailException("Mail subject is required");
        }
        if (body == null || body.isBlank()) {
            throw new MailException("Mail body is required");
        }
    }
}
