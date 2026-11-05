package com.warehouse.mail.infrastructure.adapter.primary.mapper;

import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.vo.EmailAddress;
import com.warehouse.mail.api.event.SupplierDriverLicenseInvalidatedIntegrationEvent;

import java.util.List;
import java.util.Map;

public class SupplierMailRequestMapper {

    private static final String DRIVER_LICENSE_INVALIDATED_TEMPLATE_KEY = "supplier.driver-license.invalidated";

    private final MailLocaleResolver mailLocaleResolver;

    public SupplierMailRequestMapper(final MailLocaleResolver mailLocaleResolver) {
        this.mailLocaleResolver = mailLocaleResolver;
    }

    public List<MailRequest> mapDriverLicenseInvalidated(final SupplierDriverLicenseInvalidatedIntegrationEvent event) {
        return EmailAddress.parse(event.email())
                .map(recipient -> new MailRequest(DRIVER_LICENSE_INVALIDATED_TEMPLATE_KEY,
                        this.mailLocaleResolver.defaultLocale(), recipient, Map.of(
                        "firstName", text(event.firstName()),
                        "supplierCode", event.supplierCode().value())))
                .map(List::of)
                .orElse(List.of());
    }

    private String text(final String value) {
        return value == null ? "" : value;
    }
}
