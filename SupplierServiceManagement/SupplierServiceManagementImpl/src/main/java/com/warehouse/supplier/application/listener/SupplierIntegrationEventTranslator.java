package com.warehouse.supplier.application.listener;

import com.warehouse.supplier.api.event.SupplierAnonymizedIntegrationEvent;
import com.warehouse.supplier.api.event.SupplierDeactivatedIntegrationEvent;
import com.warehouse.supplier.domain.event.SupplierAnonymized;
import com.warehouse.supplier.domain.event.SupplierDeactivated;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

@Component
public class SupplierIntegrationEventTranslator {

    @IntegrationEventTranslation
    public SupplierDeactivatedIntegrationEvent translate(final SupplierDeactivated event) {
        final SupplierSnapshot snapshot = event.getSnapshot();
        return new SupplierDeactivatedIntegrationEvent(
                snapshot.supplierId(), snapshot.supplierCode(), event.getTimestamp());
    }

    @IntegrationEventTranslation
    public SupplierAnonymizedIntegrationEvent translate(final SupplierAnonymized event) {
        final SupplierSnapshot snapshot = event.getSnapshot();
        return new SupplierAnonymizedIntegrationEvent(
                snapshot.supplierId(), snapshot.supplierCode(), event.getTimestamp());
    }
}
