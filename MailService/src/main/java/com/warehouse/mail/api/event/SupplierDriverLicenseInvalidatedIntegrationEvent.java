package com.warehouse.mail.api.event;

import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.SupplierCode;

import java.time.Instant;
import java.util.Objects;

@IntegrationEventType(value = "supplier.driver-license.invalidated", version = 1)
public record SupplierDriverLicenseInvalidatedIntegrationEvent(
        SupplierCode supplierCode,
        String email,
        String firstName,
        Instant timestamp) implements IntegrationEvent, IntegrationEventKey {

    public SupplierDriverLicenseInvalidatedIntegrationEvent {
        Objects.requireNonNull(supplierCode, "Supplier code cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    @Override
    public String eventKey() {
        return this.supplierCode.value();
    }
}
