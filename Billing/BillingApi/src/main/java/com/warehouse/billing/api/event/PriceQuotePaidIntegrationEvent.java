package com.warehouse.billing.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.billing.api.event.snapshot.PriceQuotePaidData;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "billing.price-quote-paid", version = 1, topic = BillingTopics.PRICE_QUOTE_PAID)
public class PriceQuotePaidIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final PriceQuotePaidData payload;

    @JsonCreator
    public PriceQuotePaidIntegrationEvent(@JsonProperty("payload") final PriceQuotePaidData payload) {
        this.payload = payload;
    }

    @JsonProperty("payload")
    public PriceQuotePaidData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.payload.priceQuoteId().value());
    }
}
