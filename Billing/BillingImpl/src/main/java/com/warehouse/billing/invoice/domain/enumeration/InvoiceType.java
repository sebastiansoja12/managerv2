package com.warehouse.billing.invoice.domain.enumeration;

public enum InvoiceType {
    INVOICE("FV"),
    CORRECTION("KOR");

    private final String prefix;

    InvoiceType(final String prefix) {
        this.prefix = prefix;
    }

    public String prefix() {
        return prefix;
    }
}
