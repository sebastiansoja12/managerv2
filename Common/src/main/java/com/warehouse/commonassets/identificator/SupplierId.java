package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public record SupplierId(Long value) implements ObjectValue<Long> {

}
