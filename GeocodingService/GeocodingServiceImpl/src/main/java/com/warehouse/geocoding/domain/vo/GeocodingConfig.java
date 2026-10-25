package com.warehouse.geocoding.domain.vo;

import com.warehouse.commonassets.enumeration.GeocodingProvider;

public record GeocodingConfig(GeocodingProvider provider, String apiKey, String baseUrl, String username, String password) {
}
