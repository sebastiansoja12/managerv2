package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public record ExternalId<T>(T value) {
}

