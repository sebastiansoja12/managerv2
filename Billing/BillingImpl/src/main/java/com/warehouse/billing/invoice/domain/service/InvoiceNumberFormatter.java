package com.warehouse.billing.invoice.domain.service;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;

import java.time.YearMonth;

public class InvoiceNumberFormatter {

    public String format(final InvoiceType type, final YearMonth month, final long sequence) {
        return String.format("%s/%04d/%02d/%04d", type.prefix(), month.getYear(), month.getMonthValue(), sequence);
    }
}
