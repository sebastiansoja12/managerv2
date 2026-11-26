package com.warehouse.billing.payment.application.port.primary.command;

import com.warehouse.commonassets.identificator.PriceQuoteId;

public record OpenPaymentCommand(PriceQuoteId priceQuoteId, boolean paymentRequired) {
}
