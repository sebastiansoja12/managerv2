package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.SupplierMailRequestMapper;
import com.warehouse.mail.api.event.SupplierDriverLicenseInvalidatedIntegrationEvent;

public class SupplierMailKafkaListener {

    private final MailPort mailPort;
    private final SupplierMailRequestMapper supplierMailRequestMapper;

    public SupplierMailKafkaListener(final MailPort mailPort,
                                     final SupplierMailRequestMapper supplierMailRequestMapper) {
        this.mailPort = mailPort;
        this.supplierMailRequestMapper = supplierMailRequestMapper;
    }

    @IntegrationEventListener(group = "manager-mail-supplier-driver-license-invalidated")
    public void handle(final SupplierDriverLicenseInvalidatedIntegrationEvent event) {
        this.mailPort.send(this.supplierMailRequestMapper.mapDriverLicenseInvalidated(event));
    }
}
