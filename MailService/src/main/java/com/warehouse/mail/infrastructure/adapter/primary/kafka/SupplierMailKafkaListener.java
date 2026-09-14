package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
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

    @KafkaEventListener(
            topics = "${manager.kafka.topics.supplier-driver-license-invalidated:supplier.driver-license.invalidated}",
            groupId = "${manager.mail.kafka.consumer-groups.supplier-driver-license-invalidated:manager-mail-supplier-driver-license-invalidated}"
    )
    public void handle(final SupplierDriverLicenseInvalidatedIntegrationEvent event) {
        this.mailPort.send(this.supplierMailRequestMapper.mapDriverLicenseInvalidated(event));
    }
}
