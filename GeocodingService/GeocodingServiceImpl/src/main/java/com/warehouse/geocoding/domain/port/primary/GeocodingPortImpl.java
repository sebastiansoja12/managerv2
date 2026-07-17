package com.warehouse.geocoding.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.service.GeocodingService;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationCreateCommand;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationUpdateCommand;

public class GeocodingPortImpl implements GeocodingPort {

    private final GeocodingService geocodingService;

    public GeocodingPortImpl(final GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @Override
    public void create(final GeocodingConfigurationCreateCommand command) {
        final GeocodingConfiguration configuration = new GeocodingConfiguration(
                GeocodingConfigurationId.generate(),
                command.provider().getUrl(),
                command.apiUserName(),
                command.apiPassword(),
                command.apiKey(),
                command.accessToken(),
                command.refreshToken(),
                command.enabled(),
                command.provider());
        this.geocodingService.create(configuration);
    }

    @Override
    public void update(final GeocodingConfigurationUpdateCommand command) {
        geocodingService.update(command);
    }

    @Override
    public void delete(final GeocodingConfigurationId geocodingConfigurationId) {
        geocodingService.delete(geocodingConfigurationId);
    }

    @Override
    public GeocodingConfiguration get(final GeocodingConfigurationId geocodingConfigurationId) {
        return geocodingService.get(geocodingConfigurationId);
    }

    @Override
    public GeocodingConfiguration getByProvider(final GeocodingProvider provider) {
        return geocodingService.getByProvider(provider);
    }

    @Override
    public List<GeocodingConfiguration> getAll() {
        return geocodingService.getAll();
    }
}
