package com.warehouse.mail.domain.model;

import com.warehouse.mail.domain.exception.MailException;
import com.warehouse.mail.domain.vo.EmailAddress;

import java.util.Locale;
import java.util.Map;

public record MailRequest(String templateKey, Locale locale, EmailAddress recipient, Map<String, String> variables) {

    public MailRequest {
        if (templateKey == null || templateKey.isBlank()) {
            throw new MailException("Mail template key is required");
        }
        if (locale == null) {
            throw new MailException("Mail locale is required");
        }
        if (recipient == null) {
            throw new MailException("Mail recipient is required");
        }
        variables = variables == null ? Map.of() : Map.copyOf(variables);
    }
}
