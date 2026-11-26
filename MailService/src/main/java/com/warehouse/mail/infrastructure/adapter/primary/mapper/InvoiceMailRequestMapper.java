package com.warehouse.mail.infrastructure.adapter.primary.mapper;

import com.warehouse.billing.api.event.InvoiceIssuedIntegrationEvent;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.vo.EmailAddress;
import com.warehouse.mail.domain.vo.MailAttachmentReference;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InvoiceMailRequestMapper {

    private static final String INVOICE_ISSUED_TEMPLATE_KEY = "invoice.issued";

    private final MailLocaleResolver mailLocaleResolver;

    public InvoiceMailRequestMapper(final MailLocaleResolver mailLocaleResolver) {
        this.mailLocaleResolver = mailLocaleResolver;
    }

    public List<MailRequest> mapIssued(final InvoiceIssuedIntegrationEvent event) {
        return EmailAddress.parse(event.buyerEmail())
                .map(recipient -> new MailRequest(INVOICE_ISSUED_TEMPLATE_KEY, locale(event.buyerLanguage()),
                        recipient, Map.of("invoiceNumber", event.number()),
                        List.of(new MailAttachmentReference(event.documentId()))))
                .map(List::of)
                .orElse(List.of());
    }

    private Locale locale(final String language) {
        return language == null || language.isBlank()
                ? this.mailLocaleResolver.defaultLocale()
                : Locale.forLanguageTag(language);
    }
}
