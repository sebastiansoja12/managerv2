package com.warehouse.billing.invoice.infrastructure.adapter.primary.api;

import com.warehouse.billing.api.dto.PriceQuoteIdDto;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record InvoiceApiResponse(InvoiceIdDto invoiceId,
                                 String number,
                                 InvoiceType type,
                                 InvoiceIdDto correctedInvoiceId,
                                 String correctedInvoiceNumber,
                                 PaymentIdDto paymentId,
                                 PriceQuoteIdDto priceQuoteId,
                                 PaymentMethod paymentMethod,
                                 LocalDate issueDate,
                                 LocalDate saleDate,
                                 InvoicePartyApi seller,
                                 InvoicePartyApi buyer,
                                 List<InvoiceLineApi> lines,
                                 BigDecimal net,
                                 BigDecimal vat,
                                 BigDecimal gross,
                                 Currency currency,
                                 Instant issuedAt,
                                 boolean documentAvailable) {
}
