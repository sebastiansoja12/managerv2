package com.warehouse.geocoding.infrastructure.adapter.primary.api;

import java.util.List;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.enumeration.GeocodingProvider.ConfigurationField;

public record GeocodingProviderApiResponse(GeocodingProvider provider,
                                           String url,
                                           List<ConfigurationField> activeFields) {

    public static GeocodingProviderApiResponse from(final GeocodingProvider provider) {
        return new GeocodingProviderApiResponse(provider, provider.getUrl(), provider.getActiveFields());
    }
}
