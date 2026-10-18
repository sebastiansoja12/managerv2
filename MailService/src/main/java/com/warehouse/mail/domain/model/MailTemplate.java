package com.warehouse.mail.domain.model;

import com.warehouse.mail.domain.exception.MailException;

import java.util.Locale;

public record MailTemplate(String key, Locale locale, String subject, String body) {

    public MailTemplate {
        if (key == null || key.isBlank() || locale == null || subject == null || subject.isBlank()
                || body == null || body.isBlank()) {
            throw new MailException("Mail template " + key + " must define key, locale, subject and body");
        }
    }
}
