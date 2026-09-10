package com.warehouse.geocoding.domain.port.primary;

import static com.warehouse.geocoding.GeocodingTestFixtures.CONFIGURATION_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.configuration;
import static com.warehouse.geocoding.GeocodingTestFixtures.createCommand;
import static com.warehouse.geocoding.GeocodingTestFixtures.updateCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.service.GeocodingService;

@ExtendWith(MockitoExtension.class)
class GeocodingPortImplTest {

    @Mock
    private GeocodingService geocodingService;

    private GeocodingPortImpl geocodingPort;

    @BeforeEach
    void setUp() {
        geocodingPort = new GeocodingPortImpl(geocodingService);
    }

    @Test
    void shouldCreateConfigurationFromCommand() {
        final ArgumentCaptor<GeocodingConfiguration> configurationCaptor =
                ArgumentCaptor.forClass(GeocodingConfiguration.class);

        geocodingPort.create(createCommand());

        verify(geocodingService).create(configurationCaptor.capture());
        final GeocodingConfiguration configuration = configurationCaptor.getValue();
        assertNotNull(configuration.getGeocodingConfigurationId());
        assertEquals(GeocodingProvider.GEOAPIFY.getUrl(), configuration.getApiUrl());
        assertEquals("api-key", configuration.getApiKey());
        assertEquals(GeocodingProvider.GEOAPIFY, configuration.getProvider());
        assertEquals(true, configuration.isDefaultProvider());
    }

    @Test
    void shouldDelegateUpdate() {
        geocodingPort.update(updateCommand());

        verify(geocodingService).update(updateCommand());
    }

    @Test
    void shouldDelegateDelete() {
        geocodingPort.delete(CONFIGURATION_ID);

        verify(geocodingService).delete(CONFIGURATION_ID);
    }

    @Test
    void shouldDelegateGet() {
        final GeocodingConfiguration configuration = configuration();
        when(geocodingService.get(CONFIGURATION_ID)).thenReturn(configuration);

        final GeocodingConfiguration result = geocodingPort.get(CONFIGURATION_ID);

        assertSame(configuration, result);
    }

    @Test
    void shouldDelegateGetByProvider() {
        final GeocodingConfiguration configuration = configuration();
        when(geocodingService.getByProvider(GeocodingProvider.GEOAPIFY)).thenReturn(configuration);

        final GeocodingConfiguration result = geocodingPort.getByProvider(GeocodingProvider.GEOAPIFY);

        assertSame(configuration, result);
    }

    @Test
    void shouldDelegateGetDefault() {
        final GeocodingConfiguration configuration = configuration();
        when(geocodingService.getDefault()).thenReturn(configuration);

        final GeocodingConfiguration result = geocodingPort.getDefault();

        assertSame(configuration, result);
    }

    @Test
    void shouldDelegateGetAll() {
        final List<GeocodingConfiguration> configurations = List.of(configuration());
        when(geocodingService.getAll()).thenReturn(configurations);

        final List<GeocodingConfiguration> result = geocodingPort.getAll();

        assertSame(configurations, result);
    }

    @Test
    void shouldGenerateConfigurationIdWhenCreating() {
        geocodingPort.create(createCommand());

        verify(geocodingService).create(any(GeocodingConfiguration.class));
    }
}
