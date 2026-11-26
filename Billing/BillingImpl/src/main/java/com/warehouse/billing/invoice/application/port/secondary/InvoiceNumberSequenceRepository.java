package com.warehouse.billing.invoice.application.port.secondary;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;

import java.time.YearMonth;

public interface InvoiceNumberSequenceRepository {

    long next(final InvoiceType type, final YearMonth period);
}
