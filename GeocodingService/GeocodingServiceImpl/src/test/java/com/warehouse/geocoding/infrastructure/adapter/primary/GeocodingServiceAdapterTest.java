package com.warehouse.geocoding.infrastructure.adapter.primary;

import static com.warehouse.geocoding.GeocodingTestFixtures.CONFIGURATION_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.configuration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.port.primary.GeocodingPort;
import com.warehouse.infrastructure.dto.GeocodingConfigurationDto;

@ExtendWith(MockitoExtension.class)
class GeocodingServiceAdapterTest {

    @Mock
    private GeocodingPort geocodingPort;

    private GeocodingServiceAdapter geocodingServiceAdapter;

    @BeforeEach
    void setUp() {
        geocodingServiceAdapter = new GeocodingServiceAdapter(geocodingPort);
    }

    @Test
    void shouldReturnConfigurationDtoByProvider() {
        when(geocodingPort.getByProvider(GeocodingProvider.GEOAPIFY)).thenReturn(configuration());

        final GeocodingConfigurationDto result =
                geocodingServiceAdapter.getGeocodingConfig(GeocodingProvider.GEOAPIFY);

        verify(geocodingPort).getByProvider(GeocodingProvider.GEOAPIFY);
        assertEquals(CONFIGURATION_ID, result.geocodingConfigurationId());
        assertEquals("api-key", result.apiKey());
        assertEquals(true, result.defaultProvider());
        assertEquals(GeocodingProvider.GEOAPIFY, result.provider());
    }

    @Test
    void shouldReturnDefaultConfigurationDto() {
        when(geocodingPort.getDefault()).thenReturn(configuration());

        final GeocodingConfigurationDto result = geocodingServiceAdapter.getDefaultGeocodingConfig();

        verify(geocodingPort).getDefault();
        assertEquals(CONFIGURATION_ID, result.geocodingConfigurationId());
        assertEquals(GeocodingProvider.GEOAPIFY.getUrl(), result.apiUrl());
        assertEquals(true, result.enabled());
    }
}
