package com.warehouse.geocoding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.geocoding.domain.port.primary.GeocodingPort;
import com.warehouse.geocoding.domain.port.primary.GeocodingPortImpl;
import com.warehouse.geocoding.domain.port.secondary.GeocodingRepository;
import com.warehouse.geocoding.domain.service.GeocodingService;
import com.warehouse.geocoding.domain.service.GeocodingServiceImpl;
import com.warehouse.geocoding.infrastructure.adapter.primary.GeocodingServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.secondary.GeocodingConfigurationRepositoryImpl;
import com.warehouse.geocoding.infrastructure.adapter.secondary.GeocodingPasswordCipher;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingConfigurationEntity;
import com.warehouse.infrastructure.GeocodingApiService;

@Configuration
public class GeocodingConfig {

    @Bean
    public GeocodingPasswordCipher geocodingPasswordCipher(
            @Value("${geocoding.encryption-key:}") final String encryptionKey) {
        return new GeocodingPasswordCipher(encryptionKey);
    }

    @Bean
    public GeocodingRepository geocodingRepository(
            final OperatorFilteredRepository<GeocodingConfigurationEntity> repository,
            final GeocodingPasswordCipher geocodingPasswordCipher) {
        return new GeocodingConfigurationRepositoryImpl(repository, geocodingPasswordCipher);
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
