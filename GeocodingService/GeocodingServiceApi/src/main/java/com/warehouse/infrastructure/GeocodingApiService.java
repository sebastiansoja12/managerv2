package com.warehouse.infrastructure;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.infrastructure.dto.GeocodingConfigurationDto;

public interface GeocodingApiService {
    GeocodingConfigurationDto getGeocodingConfig(final GeocodingProvider geocodingProvider);
}
