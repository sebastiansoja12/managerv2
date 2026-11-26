package com.warehouse.billing.invoice.infrastructure.adapter.primary.api;

public record InvoicePartyApi(String name,
                              String taxId,
                              String street,
                              String postalCode,
                              String city,
                              String countryCode) {
}
