package com.warehouse.commonassets.identificator;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record GeocodingConfigurationId(UUID value) {

    public static GeocodingConfigurationId generate() {
        return new GeocodingConfigurationId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }
}
