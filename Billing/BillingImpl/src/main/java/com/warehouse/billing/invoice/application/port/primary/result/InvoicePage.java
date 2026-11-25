package com.warehouse.billing.invoice.application.port.primary.result;

import com.warehouse.billing.invoice.domain.model.Invoice;

import java.util.List;

public record InvoicePage(List<Invoice> invoices,
                          int page,
                          int size,
                          long totalElements) {

    public InvoicePage {
        invoices = List.copyOf(invoices);
    }
}
