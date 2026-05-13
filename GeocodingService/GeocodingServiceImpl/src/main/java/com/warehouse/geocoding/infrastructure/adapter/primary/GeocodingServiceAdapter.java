package com.warehouse.geocoding.infrastructure.adapter.primary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.port.primary.GeocodingPort;
import com.warehouse.geocoding.infrastructure.adapter.primary.mapper.GeocodingConfigurationApiMapper;
import com.warehouse.infrastructure.GeocodingApiService;
import com.warehouse.infrastructure.dto.GeocodingConfigurationDto;

public class GeocodingServiceAdapter implements GeocodingApiService {

    private final GeocodingPort geocodingPort;

    public GeocodingServiceAdapter(final GeocodingPort geocodingPort) {
        this.geocodingPort = geocodingPort;
    }

    @Override
    public GeocodingConfigurationDto getGeocodingConfig(final GeocodingProvider geocodingProvider) {
        final GeocodingConfiguration configuration = geocodingPort.getByProvider(geocodingProvider);
        return GeocodingConfigurationApiMapper.toDto(configuration);
    }
}
