package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.kafka;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
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

    @IntegrationEventListener(group = "manager-supplier-configuration-lifecycle")
    public void handle(final SupplierDeactivatedIntegrationEvent event) {
        this.supplierConfigurationPort.releaseSubstitute(event.supplierId(), event.userId());
    }

    @IntegrationEventListener(group = "manager-supplier-configuration-lifecycle")
    public void handle(final SupplierAnonymizedIntegrationEvent event) {
        this.supplierConfigurationPort.removeConfiguration(event.supplierId(), event.userId());
    }
}
