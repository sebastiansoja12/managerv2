package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.mail.application.port.secondary.MailTemplateRepository;
import com.warehouse.mail.domain.model.MailTemplate;
import com.warehouse.mail.infrastructure.adapter.secondary.entity.MailTemplateEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

public class MailTemplateRepositoryImpl implements MailTemplateRepository {

    private final MailTemplateReadRepository mailTemplateReadRepository;

    public MailTemplateRepositoryImpl(final MailTemplateReadRepository mailTemplateReadRepository) {
        this.mailTemplateReadRepository = mailTemplateReadRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MailTemplate> find(final String templateKey, final Locale locale) {
        return this.mailTemplateReadRepository.findByTemplateKeyAndLocale(templateKey, locale.toLanguageTag())
                .map(MailTemplateRepositoryImpl::toModel);
    }

    private static MailTemplate toModel(final MailTemplateEntity entity) {
        return new MailTemplate(entity.getTemplateKey(), Locale.forLanguageTag(entity.getLocale()),
                entity.getSubject(), entity.getBody());
    }
}
