package com.warehouse.billing.payment.application.port.primary.command;

import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TaxId;

public record RecordCounterPaymentCommand(PriceQuoteId priceQuoteId,
                                          PaymentMethod method,
                                          TaxId buyerTaxId) {
}
