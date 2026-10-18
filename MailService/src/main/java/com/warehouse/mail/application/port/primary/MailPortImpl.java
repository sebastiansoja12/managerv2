package com.warehouse.mail.application.port.primary;

import com.warehouse.mail.application.port.secondary.MailSenderServicePort;
import com.warehouse.mail.application.port.secondary.MailTemplateRepository;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.model.MailTemplate;
import com.warehouse.mail.domain.service.MailTemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class MailPortImpl implements MailPort {

    private static final Logger log = LoggerFactory.getLogger(MailPortImpl.class);

    private final MailTemplateRepository mailTemplateRepository;
    private final MailTemplateRenderer mailTemplateRenderer;
    private final MailSenderServicePort mailSenderServicePort;
    private final Locale defaultLocale;
    private final Map<String, String> globalVariables;

    public MailPortImpl(final MailTemplateRepository mailTemplateRepository,
                        final MailTemplateRenderer mailTemplateRenderer,
                        final MailSenderServicePort mailSenderServicePort,
                        final Locale defaultLocale,
                        final Map<String, String> globalVariables) {
        this.mailTemplateRepository = mailTemplateRepository;
        this.mailTemplateRenderer = mailTemplateRenderer;
        this.mailSenderServicePort = mailSenderServicePort;
        this.defaultLocale = defaultLocale;
        this.globalVariables = Map.copyOf(globalVariables);
    }

    @Override
    public void send(final List<MailRequest> mailRequests) {
        final List<MailMessage> messages = mailRequests.stream()
                .flatMap(request -> template(request)
                        .map(template -> this.mailTemplateRenderer.render(template, withGlobalVariables(request)))
                        .stream())
                .toList();
        messages.forEach(this.mailSenderServicePort::send);
    }

    @Override
    public void send(final MailMessage mailMessage) {
        this.mailSenderServicePort.send(mailMessage);
    }

    private Optional<MailTemplate> template(final MailRequest request) {
        final Optional<MailTemplate> template = this.mailTemplateRepository.find(request.templateKey(), request.locale())
                .or(() -> languageFallback(request))
                .or(() -> this.mailTemplateRepository.find(request.templateKey(), this.defaultLocale));
        if (template.isEmpty()) {
            log.info("No mail template {} for {} or {}, e-mail skipped", request.templateKey(),
                    request.locale().toLanguageTag(), this.defaultLocale.toLanguageTag());
        }
        return template;
    }

    private MailRequest withGlobalVariables(final MailRequest request) {
        final Map<String, String> variables = new HashMap<>(this.globalVariables);
        variables.putAll(request.variables());
        return new MailRequest(request.templateKey(), request.locale(), request.recipient(), variables);
    }

    private Optional<MailTemplate> languageFallback(final MailRequest request) {
        final Locale language = Locale.of(request.locale().getLanguage());
        return language.equals(request.locale()) ? Optional.empty()
                : this.mailTemplateRepository.find(request.templateKey(), language);
    }
}
