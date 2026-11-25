package com.warehouse.billing.invoice.domain.vo;

import com.warehouse.commonassets.identificator.TaxId;

public record InvoiceParty(String name, TaxId taxId, String street, String postalCode, String city,
                           String countryCode, String email) {

    public static InvoiceParty unknown(final TaxId taxId) {
        return new InvoiceParty(null, taxId, null, null, null, null, null);
    }
}
