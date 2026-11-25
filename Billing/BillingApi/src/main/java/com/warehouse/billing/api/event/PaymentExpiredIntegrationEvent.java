package com.warehouse.billing.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "billing.payment-expired", version = 1, topic = BillingTopics.PAYMENT_EXPIRED)
public class PaymentExpiredIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final PriceQuoteId priceQuoteId;

    private final PaymentId paymentId;

    private final Instant expiredAt;

    @JsonCreator
    public PaymentExpiredIntegrationEvent(@JsonProperty("priceQuoteId") final PriceQuoteId priceQuoteId,
                                          @JsonProperty("paymentId") final PaymentId paymentId,
                                          @JsonProperty("expiredAt") final Instant expiredAt) {
        this.priceQuoteId = priceQuoteId;
        this.paymentId = paymentId;
        this.expiredAt = expiredAt;
    }

    @JsonProperty("priceQuoteId")
    public PriceQuoteId priceQuoteId() {
        return priceQuoteId;
    }

    @JsonProperty("paymentId")
    public PaymentId paymentId() {
        return paymentId;
    }

    @JsonProperty("expiredAt")
    public Instant expiredAt() {
        return expiredAt;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.priceQuoteId.value());
    }
}
