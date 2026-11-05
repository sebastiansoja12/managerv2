package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.mail.api.event.SupplierDriverLicenseInvalidatedIntegrationEvent;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.MailServicePort;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

@Slf4j
public class MailServiceAdapter implements MailServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public MailServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void notifyDriverLicenseInvalidated(final Supplier supplier) {
        if (StringUtils.isBlank(supplier.getEmail())) {
            log.warn("Supplier {} has no email, skipping driver license notification", supplier.supplierCode().value());
            return;
        }
        this.integrationEventPublisher.publish(new SupplierDriverLicenseInvalidatedIntegrationEvent(
                supplier.supplierCode(),
                supplier.getEmail(),
                supplier.getFirstName(),
                Instant.now()));
    }
}
