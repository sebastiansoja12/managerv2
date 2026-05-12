package com.warehouse.geocoding.domain.port.secondary;

import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;

public interface GeocodingRepository {

    void create(final GeocodingConfiguration configuration);

    void update(final GeocodingConfiguration configuration);

    void delete(final GeocodingConfigurationId geocodingConfigurationId);

    Optional<GeocodingConfiguration> findById(final GeocodingConfigurationId geocodingConfigurationId);

    Optional<GeocodingConfiguration> findByProvider(final GeocodingProvider provider);

    List<GeocodingConfiguration> findAll();
}
