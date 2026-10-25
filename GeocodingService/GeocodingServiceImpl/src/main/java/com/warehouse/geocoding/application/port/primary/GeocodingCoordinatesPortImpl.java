package com.warehouse.geocoding.application.port.primary;

import java.util.Set;

import com.warehouse.geocoding.application.port.secondary.GeocodingProviderServicePort;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;
import com.warehouse.geocoding.domain.vo.GeocodingConfig;
import com.warehouse.geocoding.application.port.primary.GeocodingPort;

public class GeocodingCoordinatesPortImpl implements GeocodingCoordinatesPort {

    private final GeocodingPort geocodingPort;
    private final Set<GeocodingProviderServicePort> geolocationServiceProvider;

    public GeocodingCoordinatesPortImpl(final GeocodingPort geocodingPort,
                                        final Set<GeocodingProviderServicePort> geolocationServiceProvider) {
        this.geocodingPort = geocodingPort;
        this.geolocationServiceProvider = geolocationServiceProvider;
    }

    @Override
    public Coordinates obtainCoordinates(final GeocodingAddress address) {
        final GeocodingConfiguration configuration = geocodingPort.getDefault();
        final GeocodingConfig geocodingConfig = new GeocodingConfig(configuration.getProvider(),
                configuration.getApiKey(), configuration.getApiUrl(), configuration.getApiUserName(),
                configuration.getApiPassword());
        return geolocationServiceProvider.stream()
                .filter(e -> geocodingConfig != null)
                .filter(provider -> provider.canHandle(geocodingConfig.provider()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("External system for finding geolocations not configured for this operator!!!"))
                .obtainCoordinates(address, geocodingConfig);
    }
}
