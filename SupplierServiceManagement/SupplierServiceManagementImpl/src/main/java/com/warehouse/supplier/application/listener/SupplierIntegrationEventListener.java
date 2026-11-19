package com.warehouse.supplier.application.listener;

import com.warehouse.supplier.api.event.SupplierAnonymizedIntegrationEvent;
import com.warehouse.supplier.api.event.SupplierDeactivatedIntegrationEvent;
import com.warehouse.supplier.application.port.secondary.SupplierIntegrationEventServicePort;
import com.warehouse.supplier.domain.event.SupplierAnonymized;
import com.warehouse.supplier.domain.event.SupplierDeactivated;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class SupplierIntegrationEventListener {

    private final SupplierIntegrationEventServicePort supplierIntegrationEventServicePort;

    public SupplierIntegrationEventListener(
            final SupplierIntegrationEventServicePort supplierIntegrationEventServicePort) {
        this.supplierIntegrationEventServicePort = supplierIntegrationEventServicePort;
    }

    @EventListener
    public void handle(final SupplierDeactivated event) {
        final SupplierSnapshot snapshot = event.getSnapshot();
        this.supplierIntegrationEventServicePort.publishEvent(new SupplierDeactivatedIntegrationEvent(
                snapshot.supplierId(), snapshot.supplierCode(), event.getTimestamp()));
    }

    @EventListener
    public void handle(final SupplierAnonymized event) {
        final SupplierSnapshot snapshot = event.getSnapshot();
        this.supplierIntegrationEventServicePort.publishEvent(new SupplierAnonymizedIntegrationEvent(
                snapshot.supplierId(), snapshot.supplierCode(), event.getTimestamp()));
    }
}
