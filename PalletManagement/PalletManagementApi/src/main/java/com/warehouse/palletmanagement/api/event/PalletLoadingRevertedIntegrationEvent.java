package com.warehouse.palletmanagement.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadingRevertedEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "pallet.loading-reverted", version = 1, topic = PalletTopics.LOADING_REVERTED)
public class PalletLoadingRevertedIntegrationEvent extends OperatorAwareContext implements IntegrationEvent, IntegrationEventKey {

    private final PalletLoadingRevertedEventData payload;

    @JsonCreator
    public PalletLoadingRevertedIntegrationEvent(@JsonProperty("payload") final PalletLoadingRevertedEventData payload) {
        this.payload = payload;
    }

    @JsonProperty("payload")
    public PalletLoadingRevertedEventData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return this.payload.palletId();
    }
}
