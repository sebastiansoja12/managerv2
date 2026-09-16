package com.warehouse.geocoding.configuration;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;
import com.warehouse.geocoding.api.PostalCodeService;
import com.warehouse.geocoding.application.port.primary.GeocodingCoordinatesPort;
import com.warehouse.geocoding.application.port.primary.GeocodingCoordinatesPortImpl;
import com.warehouse.geocoding.application.port.secondary.GeocodingProviderServicePort;
import com.warehouse.geocoding.application.port.primary.GeocodingPort;
import com.warehouse.geocoding.application.port.primary.GeocodingPortImpl;
import com.warehouse.geocoding.application.port.primary.PostalCodePort;
import com.warehouse.geocoding.application.port.primary.PostalCodePortImpl;
import com.warehouse.geocoding.application.port.secondary.GeocodingRepository;
import com.warehouse.geocoding.application.port.secondary.PostalCodeRepository;
import com.warehouse.geocoding.application.service.GeocodingService;
import com.warehouse.geocoding.application.service.GeocodingServiceImpl;
import com.warehouse.geocoding.infrastructure.adapter.primary.GeocodingServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.primary.GeocodingCoordinatesServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.primary.PostalCodeServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.secondary.GeocodingConfigurationRepositoryImpl;
import com.warehouse.geocoding.infrastructure.adapter.secondary.GeolocationGeoapifyClientServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.secondary.GeolocationGeocodifyClientServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.secondary.GeolocationOpenCageClientServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.secondary.GeolocationPositionStackClientServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.secondary.PostalCodeReadRepository;
import com.warehouse.geocoding.infrastructure.adapter.secondary.PostalCodeRepositoryImpl;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingConfigurationEntity;
import com.warehouse.infrastructure.GeocodingApiService;

@Configuration
public class GeocodingConfig {

    @Bean
    public GeocodingRepository geocodingRepository(
            final OperatorFilteredRepository<GeocodingConfigurationEntity> repository,
            final CredentialCipher credentialCipher) {
        return new GeocodingConfigurationRepositoryImpl(repository, credentialCipher);
    }

    @Bean
    public GeocodingService geocodingService(final GeocodingRepository geocodingRepository) {
        return new GeocodingServiceImpl(geocodingRepository);
    }

    @Bean
    public GeocodingPort geocodingPort(final GeocodingService geocodingService) {
        return new GeocodingPortImpl(geocodingService);
    }

    @Bean
    public GeocodingApiService geocodingApiService(final GeocodingPort geocodingPort) {
        return new GeocodingServiceAdapter(geocodingPort);
    }

    @Bean
    public GeocodingProviderServicePort positionStackProviderServicePort() {
        return new GeolocationPositionStackClientServiceAdapter();
    }

    @Bean
    public GeocodingProviderServicePort geoapifyProviderServicePort() {
        return new GeolocationGeoapifyClientServiceAdapter();
    }

    @Bean
    public GeocodingProviderServicePort openCageProviderServicePort() {
        return new GeolocationOpenCageClientServiceAdapter();
    }

    @Bean
    public GeocodingProviderServicePort geocodifyProviderServicePort() {
        return new GeolocationGeocodifyClientServiceAdapter();
    }

    @Bean
    public GeocodingCoordinatesPort geocodingCoordinatesPort(final GeocodingPort geocodingPort,
            final Set<GeocodingProviderServicePort> providers) {
        return new GeocodingCoordinatesPortImpl(geocodingPort, providers);
    }

    @Bean
    public GeocodingCoordinatesService geocodingCoordinatesService(final GeocodingCoordinatesPort port) {
        return new GeocodingCoordinatesServiceAdapter(port);
    }

    @Bean
    public PostalCodeRepository postalCodeRepository(final PostalCodeReadRepository repository) {
        return new PostalCodeRepositoryImpl(repository);
    }

    @Bean
    public PostalCodePort postalCodePort(final PostalCodeRepository postalCodeRepository) {
        return new PostalCodePortImpl(postalCodeRepository);
    }

    @Bean
    public PostalCodeService postalCodeService(final PostalCodePort postalCodePort) {
        return new PostalCodeServiceAdapter(postalCodePort);
    }
}
