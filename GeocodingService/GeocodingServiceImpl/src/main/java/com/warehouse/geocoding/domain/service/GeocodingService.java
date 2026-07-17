package com.warehouse.geocoding.domain.service;

import java.util.List;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationUpdateCommand;

public interface GeocodingService {

    void create(final GeocodingConfiguration configuration);

    void update(final GeocodingConfigurationUpdateCommand command);

    void delete(final GeocodingConfigurationId geocodingConfigurationId);

    GeocodingConfiguration get(final GeocodingConfigurationId geocodingConfigurationId);

    GeocodingConfiguration getByProvider(final GeocodingProvider provider);

    List<GeocodingConfiguration> getAll();
}
