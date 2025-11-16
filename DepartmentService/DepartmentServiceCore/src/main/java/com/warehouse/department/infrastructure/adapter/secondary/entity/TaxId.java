package com.warehouse.department.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record TaxId(@Column(name = "tax_id") String value) {


    public TaxId() {
        this(null);
    }
}
