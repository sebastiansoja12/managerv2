package com.warehouse.billing.api.event.snapshot;

import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;

import java.time.Instant;

public record PriceQuotePaidData(PriceQuoteId priceQuoteId, PaymentId paymentId, Instant paidAt) {
}
