package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.supplier.api.event.SupplierAnonymizedIntegrationEvent;
import com.warehouse.supplier.api.event.SupplierDeactivatedIntegrationEvent;
import com.warehouse.supplierconfiguration.application.port.primary.SupplierConfigurationPort;
import org.springframework.stereotype.Component;

@Component
public class SupplierLifecycleIntegrationListener {

    private final SupplierConfigurationPort supplierConfigurationPort;

    public SupplierLifecycleIntegrationListener(final SupplierConfigurationPort supplierConfigurationPort) {
        this.supplierConfigurationPort = supplierConfigurationPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.supplier-deactivated:supplier.deactivated}",
            groupId = "${manager.kafka.consumer-groups.supplier-configuration-lifecycle:manager-supplier-configuration-lifecycle}"
    )
    public void handle(final SupplierDeactivatedIntegrationEvent event) {
        this.supplierConfigurationPort.releaseSubstitute(event.supplierId(), event.userId());
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.supplier-anonymized:supplier.anonymized}",
            groupId = "${manager.kafka.consumer-groups.supplier-configuration-lifecycle:manager-supplier-configuration-lifecycle}"
    )
    public void handle(final SupplierAnonymizedIntegrationEvent event) {
        this.supplierConfigurationPort.removeConfiguration(event.supplierId(), event.userId());
    }
}
