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
@IntegrationEventType(value = "supplier.anonymized", version = 1)
public class SupplierAnonymizedIntegrationEvent extends OperatorAwareContext implements IntegrationEvent, IntegrationEventKey {

    private final SupplierId supplierId;

    private final SupplierCode supplierCode;

    private final Instant anonymizedAt;

    @JsonCreator
    public SupplierAnonymizedIntegrationEvent(@JsonProperty("supplierId") final SupplierId supplierId,
            @JsonProperty("supplierCode") final SupplierCode supplierCode,
            @JsonProperty("anonymizedAt") final Instant anonymizedAt) {
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.anonymizedAt = anonymizedAt;
    }

    @JsonProperty("supplierId")
    public SupplierId supplierId() {
        return supplierId;
    }

    @JsonProperty("supplierCode")
    public SupplierCode supplierCode() {
        return supplierCode;
    }

    @JsonProperty("anonymizedAt")
    public Instant anonymizedAt() {
        return anonymizedAt;
    }

    @Override
    public String eventKey() {
        return String.valueOf(supplierId.value());
    }
}
