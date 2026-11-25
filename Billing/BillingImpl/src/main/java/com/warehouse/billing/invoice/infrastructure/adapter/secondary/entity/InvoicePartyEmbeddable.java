package com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.TaxId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class InvoicePartyEmbeddable {

    @Column(name = "name")
    private String name;

    @Embedded
    private TaxId taxId;

    @Column(name = "street")
    private String street;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "email")
    private String email;

    public InvoicePartyEmbeddable(final String name, final TaxId taxId, final String street,
                                  final String postalCode, final String city, final String countryCode,
                                  final String email) {
        this.name = name;
        this.taxId = taxId;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.countryCode = countryCode;
        this.email = email;
    }
}
