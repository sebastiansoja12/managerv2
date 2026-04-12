package com.warehouse.geocoding.domain.service;

import java.util.List;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.port.secondary.GeocodingRepository;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationUpdateCommand;

public class GeocodingServiceImpl implements GeocodingService {

    private static final String NOT_FOUND_TYPE = "https://warehouse.dev/problems/geocoding-configuration-not-found";
    private static final String NOT_FOUND_TITLE = "Geocoding Configuration Not Found";

    private final GeocodingRepository geocodingRepository;

    public GeocodingServiceImpl(final GeocodingRepository geocodingRepository) {
        this.geocodingRepository = geocodingRepository;
    }

    @Override
    public void create(final GeocodingConfiguration configuration) {
        geocodingRepository.create(configuration);
    }

    @Override
    public void update(final GeocodingConfigurationUpdateCommand command) {
        final GeocodingConfiguration configuration = get(command.geocodingConfigurationId());
        configuration.update(command);
        geocodingRepository.update(configuration);
    }

    @Override
    public void delete(final GeocodingConfigurationId geocodingConfigurationId) {
        get(geocodingConfigurationId);
        geocodingRepository.delete(geocodingConfigurationId);
    }

    @Override
    public GeocodingConfiguration get(final GeocodingConfigurationId geocodingConfigurationId) {
        return geocodingRepository.findById(geocodingConfigurationId)
                .orElseThrow(() -> notFound("Geocoding configuration with id "
                        + geocodingConfigurationId.value() + " was not found"));
    }

    @Override
    public GeocodingConfiguration getByProvider(final GeocodingProvider provider) {
        return geocodingRepository.findByProvider(provider)
                .orElseThrow(() -> notFound("Geocoding configuration for provider "
                        + provider + " was not found"));
    }

    @Override
    public List<GeocodingConfiguration> getAll() {
        return geocodingRepository.findAll();
    }

    private ProblemDetailsException notFound(final String detail) {
        return new ProblemDetailsException(NOT_FOUND_TYPE, NOT_FOUND_TITLE, 404, detail);
    }
}
