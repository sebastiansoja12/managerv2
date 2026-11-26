package com.warehouse.billing.invoice.domain.service;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.vo.InvoiceDraft;
import com.warehouse.billing.invoice.domain.vo.InvoiceLine;
import com.warehouse.commonassets.identificator.InvoiceId;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;

public class InvoiceFactory {

    private final InvoiceNumberFormatter invoiceNumberFormatter;

    private final ZoneId businessZone;

    private final Clock clock;

    public InvoiceFactory(final InvoiceNumberFormatter invoiceNumberFormatter, final ZoneId businessZone,
                          final Clock clock) {
        this.invoiceNumberFormatter = invoiceNumberFormatter;
        this.businessZone = businessZone;
        this.clock = clock;
    }

    public YearMonth period(final Instant instant) {
        return YearMonth.from(instant.atZone(this.businessZone));
    }

    public Invoice create(final InvoiceDraft draft, final long sequence) {
        final LocalDate paidOn = LocalDate.ofInstant(draft.paidAt(), this.businessZone);
        return new Invoice(InvoiceId.nextId(),
                this.invoiceNumberFormatter.format(InvoiceType.INVOICE, YearMonth.from(paidOn), sequence),
                InvoiceType.INVOICE, null, null, draft.paymentId(), draft.priceQuoteId(), draft.paymentMethod(),
                paidOn, paidOn, draft.seller().party(), draft.seller().bankAccount(), draft.buyer().party(),
                draft.buyer().language(), draft.currency(), draft.lines(), this.clock.instant(), null, null);
    }

    public Invoice createCorrection(final Invoice original, final Instant refundedAt, final long sequence) {
        final LocalDate refundedOn = LocalDate.ofInstant(refundedAt, this.businessZone);
        return new Invoice(InvoiceId.nextId(),
                this.invoiceNumberFormatter.format(InvoiceType.CORRECTION, YearMonth.from(refundedOn), sequence),
                InvoiceType.CORRECTION, original.id(), original.number(), original.paymentId(),
                original.priceQuoteId(), original.paymentMethod(), refundedOn, original.saleDate(), original.seller(),
                original.sellerBankAccount(), original.buyer(), original.buyerLanguage(), original.currency(),
                original.lines().stream().map(InvoiceLine::negate).toList(), this.clock.instant(), null, null);
    }
}
