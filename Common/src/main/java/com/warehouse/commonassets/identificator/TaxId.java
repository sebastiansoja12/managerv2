package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class TaxId {

    private String value;

    public TaxId() {
    }

    public TaxId(final String value) {
        this.value = value;
    }

    public static TaxId of(final String value) {
        return new TaxId(value);
    }

    public String value() {
        return value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxId taxId)) {
            return false;
        }
        return Objects.equals(value, taxId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
