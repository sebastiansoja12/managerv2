package com.warehouse.billing.invoice.domain.vo;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;

import java.util.UUID;

public record InvoiceSnapshot(InvoiceId invoiceId, String number, InvoiceType type, InvoiceId correctedInvoiceId,
                              PaymentId paymentId, PriceQuoteId priceQuoteId, Money gross, String buyerEmail,
                              String buyerLanguage, UUID documentId) {
}
