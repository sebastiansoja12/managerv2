package com.warehouse.commonassets.identificator;


import jakarta.persistence.Embeddable;

@Embeddable
public record SupplierCode(String value) implements ObjectValue<String> {
}
