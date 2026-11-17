package com.warehouse.palletmanagement.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.palletmanagement.api.event.snapshot.PalletDepartmentsChangedEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "pallet.departments-changed", version = 1)
public class PalletDepartmentsChangedIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final PalletDepartmentsChangedEventData payload;

    @JsonCreator
    public PalletDepartmentsChangedIntegrationEvent(
            @JsonProperty("payload") final PalletDepartmentsChangedEventData payload) {
        this.payload = payload;
    }

    @JsonProperty("payload")
    public PalletDepartmentsChangedEventData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return this.payload.palletId().value();
    }
}
