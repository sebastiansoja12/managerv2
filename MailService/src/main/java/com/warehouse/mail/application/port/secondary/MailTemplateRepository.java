package com.warehouse.mail.application.port.secondary;

import com.warehouse.mail.domain.model.MailTemplate;

import java.util.Locale;
import java.util.Optional;

public interface MailTemplateRepository {
    Optional<MailTemplate> find(final String templateKey, final Locale locale);
}
