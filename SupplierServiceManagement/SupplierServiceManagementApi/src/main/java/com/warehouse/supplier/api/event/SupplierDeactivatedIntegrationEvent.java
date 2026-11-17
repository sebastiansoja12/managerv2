package com.warehouse.supplier.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "supplier.deactivated", version = 1)
public class SupplierDeactivatedIntegrationEvent extends OperatorAwareContext implements IntegrationEvent, IntegrationEventKey {

    private final SupplierId supplierId;

    private final SupplierCode supplierCode;

    private final Instant deactivatedAt;

    @JsonCreator
    public SupplierDeactivatedIntegrationEvent(@JsonProperty("supplierId") final SupplierId supplierId,
            @JsonProperty("supplierCode") final SupplierCode supplierCode,
            @JsonProperty("deactivatedAt") final Instant deactivatedAt) {
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.deactivatedAt = deactivatedAt;
    }

    @JsonProperty("supplierId")
    public SupplierId supplierId() {
        return supplierId;
    }

    @JsonProperty("supplierCode")
    public SupplierCode supplierCode() {
        return supplierCode;
    }

    @JsonProperty("deactivatedAt")
    public Instant deactivatedAt() {
        return deactivatedAt;
    }

    @Override
    public String eventKey() {
        return String.valueOf(supplierId.value());
    }
}
