package com.warehouse.geocoding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.geocoding.domain.port.primary.GeocodingPort;
import com.warehouse.geocoding.domain.port.primary.GeocodingPortImpl;
import com.warehouse.geocoding.domain.port.secondary.GeocodingRepository;
import com.warehouse.geocoding.domain.service.GeocodingService;
import com.warehouse.geocoding.domain.service.GeocodingServiceImpl;
import com.warehouse.geocoding.infrastructure.adapter.primary.GeocodingServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.secondary.GeocodingConfigurationRepositoryImpl;
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
}
