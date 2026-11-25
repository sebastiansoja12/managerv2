package com.warehouse.mail.domain.model;

import com.warehouse.mail.domain.exception.MailException;
import com.warehouse.mail.domain.vo.EmailAddress;
import com.warehouse.mail.domain.vo.MailAttachment;

import java.util.List;

public record MailMessage(EmailAddress recipient, String subject, String body, List<MailAttachment> attachments) {

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
        attachments = attachments == null ? List.of() : List.copyOf(attachments);
    }

    public MailMessage(final EmailAddress recipient, final String subject, final String body) {
        this(recipient, subject, body, List.of());
    }

    public MailMessage withAttachments(final List<MailAttachment> attachments) {
        return new MailMessage(recipient, subject, body, attachments);
    }
}
