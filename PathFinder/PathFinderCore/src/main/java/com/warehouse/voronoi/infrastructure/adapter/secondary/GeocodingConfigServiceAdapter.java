package com.warehouse.voronoi.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.infrastructure.GeocodingApiService;
import com.warehouse.voronoi.domain.port.secondary.GeocodingConfigServicePort;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;

public class GeocodingConfigServiceAdapter implements GeocodingConfigServicePort {

    private final GeocodingApiService geocodingApiService;

    public GeocodingConfigServiceAdapter(final GeocodingApiService geocodingApiService) {
        this.geocodingApiService = geocodingApiService;
    }

    @Override
    public GeocodingConfig findGeocodingConfig(final GeocodingProvider provider) {
        return GeocodingConfig.from(geocodingApiService.getGeocodingConfig(provider));
    }
}
