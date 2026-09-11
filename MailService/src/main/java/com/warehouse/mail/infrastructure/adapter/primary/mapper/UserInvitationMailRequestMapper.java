package com.warehouse.mail.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.api.event.UserInvitationIssuedIntegrationEvent;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.vo.EmailAddress;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserInvitationMailRequestMapper {

    private static final String TEMPLATE_KEY = "user.invitation";
    private static final DateTimeFormatter EXPIRY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.of("Europe/Warsaw"));

    private final MailLocaleResolver mailLocaleResolver;

    public UserInvitationMailRequestMapper(final MailLocaleResolver mailLocaleResolver) {
        this.mailLocaleResolver = mailLocaleResolver;
    }

    public List<MailRequest> map(final UserInvitationIssuedIntegrationEvent event) {
        return EmailAddress.parse(event.email())
                .map(recipient -> new MailRequest(TEMPLATE_KEY, locale(event.language()), recipient, Map.of(
                        "firstName", text(event.firstName()),
                        "username", text(event.username()),
                        "invitationUrl", event.invitationUrl(),
                        "expiresAt", event.expiresAt() == null ? "" : EXPIRY_FORMAT.format(event.expiresAt()))))
                .map(List::of)
                .orElse(List.of());
    }

    private Locale locale(final String language) {
        return language == null || language.isBlank()
                ? this.mailLocaleResolver.defaultLocale()
                : Locale.forLanguageTag(language);
    }

    private String text(final String value) {
        return value == null ? "" : value;
    }
}
