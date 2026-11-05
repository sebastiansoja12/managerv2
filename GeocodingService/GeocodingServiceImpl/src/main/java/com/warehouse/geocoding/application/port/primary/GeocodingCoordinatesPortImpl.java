package com.warehouse.geocoding.application.port.primary;

import com.warehouse.geocoding.application.port.secondary.GeocodingAddressRepository;
import com.warehouse.geocoding.application.port.secondary.GeocodingProviderServicePort;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;
import com.warehouse.geocoding.domain.vo.GeocodingConfig;
import com.warehouse.geocoding.domain.vo.NormalizedGeocodingAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class GeocodingCoordinatesPortImpl implements GeocodingCoordinatesPort {

    private static final Logger log = LoggerFactory.getLogger(GeocodingCoordinatesPortImpl.class);

    private final GeocodingPort geocodingPort;
    private final Set<GeocodingProviderServicePort> geolocationServiceProvider;
    private final GeocodingAddressRepository geocodingAddressRepository;

    public GeocodingCoordinatesPortImpl(final GeocodingPort geocodingPort,
                                        final Set<GeocodingProviderServicePort> geolocationServiceProvider,
                                        final GeocodingAddressRepository geocodingAddressRepository) {
        this.geocodingPort = geocodingPort;
        this.geolocationServiceProvider = geolocationServiceProvider;
        this.geocodingAddressRepository = geocodingAddressRepository;
    }

    @Override
    public Coordinates obtainCoordinates(final GeocodingAddress address) {
        final NormalizedGeocodingAddress normalizedAddress = address.normalize();
        return geocodingAddressRepository.findCoordinates(normalizedAddress)
                .orElseGet(() -> geocode(address, normalizedAddress));
    }

    private Coordinates geocode(final GeocodingAddress address, final NormalizedGeocodingAddress normalizedAddress) {
        final GeocodingConfiguration configuration = geocodingPort.getDefault();
        final GeocodingConfig geocodingConfig = new GeocodingConfig(configuration.getProvider(),
                configuration.getApiKey(), configuration.getApiUrl(), configuration.getApiUserName(),
                configuration.getApiPassword());
        final GeocodingProviderServicePort provider = geolocationServiceProvider.stream()
                .filter(p -> p.canHandle(configuration.getProvider()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("External system for finding geolocations not configured for this operator!!!"));

        final Coordinates coordinates;
        try {
            coordinates = provider.obtainCoordinates(address, geocodingConfig);
        } catch (final RuntimeException exception) {
            log.error("Geocoding provider {} failed for address '{}': {}", configuration.getProvider(),
                    address.formattedAddress(), exception.getMessage());
            throw exception;
        }

        if (coordinates == null || coordinates.lat() == null || coordinates.lon() == null) {
            log.warn("Geocoding provider {} returned no coordinates for address '{}'; result not cached",
                    configuration.getProvider(), address.formattedAddress());
            return coordinates;
        }
        if (coordinates.approximate()) {
            log.warn("Geocoding provider {} returned only an approximate match for address '{}'; result not cached",
                    configuration.getProvider(), address.formattedAddress());
            return coordinates;
        }
        geocodingAddressRepository.saveIfAbsent(normalizedAddress, coordinates, configuration.getProvider());
        return coordinates;
    }
}
