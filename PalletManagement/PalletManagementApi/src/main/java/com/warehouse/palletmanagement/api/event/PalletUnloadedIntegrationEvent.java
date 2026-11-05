package com.warehouse.palletmanagement.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.palletmanagement.api.event.snapshot.PalletUnloadedEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "pallet.unloaded", version = 1)
public class PalletUnloadedIntegrationEvent extends OperatorAwareContext implements IntegrationEvent, IntegrationEventKey {

    private final PalletUnloadedEventData payload;

    @JsonCreator
    public PalletUnloadedIntegrationEvent(@JsonProperty("payload") final PalletUnloadedEventData payload) {
        this.payload = payload;
    }

    @JsonProperty("payload")
    public PalletUnloadedEventData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return this.payload.palletId().value();
    }
}
