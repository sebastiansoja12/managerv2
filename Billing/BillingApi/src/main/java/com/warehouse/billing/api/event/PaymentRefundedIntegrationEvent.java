package com.warehouse.billing.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "billing.payment-refunded", version = 1, topic = BillingTopics.PAYMENT_REFUNDED)
public class PaymentRefundedIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final PriceQuoteId priceQuoteId;

    private final PaymentId paymentId;

    private final Money amount;

    private final Instant refundedAt;

    @JsonCreator
    public PaymentRefundedIntegrationEvent(@JsonProperty("priceQuoteId") final PriceQuoteId priceQuoteId,
                                           @JsonProperty("paymentId") final PaymentId paymentId,
                                           @JsonProperty("amount") final Money amount,
                                           @JsonProperty("refundedAt") final Instant refundedAt) {
        this.priceQuoteId = priceQuoteId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.refundedAt = refundedAt;
    }

    @JsonProperty("priceQuoteId")
    public PriceQuoteId priceQuoteId() {
        return priceQuoteId;
    }

    @JsonProperty("paymentId")
    public PaymentId paymentId() {
        return paymentId;
    }

    @JsonProperty("amount")
    public Money amount() {
        return amount;
    }

    @JsonProperty("refundedAt")
    public Instant refundedAt() {
        return refundedAt;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.priceQuoteId.value());
    }
}
