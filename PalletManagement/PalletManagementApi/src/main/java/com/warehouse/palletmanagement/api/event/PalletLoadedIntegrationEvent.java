package com.warehouse.palletmanagement.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadedEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "pallet.loaded", version = 1)
public class PalletLoadedIntegrationEvent extends OperatorAwareContext implements IntegrationEvent, IntegrationEventKey {

    private final PalletLoadedEventData payload;

    @JsonCreator
    public PalletLoadedIntegrationEvent(@JsonProperty("payload") final PalletLoadedEventData payload) {
        this.payload = payload;
    }

    @JsonProperty("payload")
    public PalletLoadedEventData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return this.payload.palletId();
    }
}
