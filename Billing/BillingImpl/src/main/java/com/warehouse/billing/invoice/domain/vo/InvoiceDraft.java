package com.warehouse.billing.invoice.domain.vo;

import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;

import java.time.Instant;
import java.util.List;

public record InvoiceDraft(PaymentId paymentId, PriceQuoteId priceQuoteId, PaymentMethod paymentMethod,
                           Instant paidAt, InvoiceSeller seller, InvoiceBuyer buyer, Currency currency,
                           List<InvoiceLine> lines) {

    public InvoiceDraft {
        lines = List.copyOf(lines);
    }
}
