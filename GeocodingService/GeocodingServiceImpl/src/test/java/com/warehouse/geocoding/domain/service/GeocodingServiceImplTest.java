package com.warehouse.geocoding.domain.service;

import static com.warehouse.geocoding.GeocodingTestFixtures.CONFIGURATION_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.OTHER_CONFIGURATION_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.configuration;
import static com.warehouse.geocoding.GeocodingTestFixtures.disabledConfiguration;
import static com.warehouse.geocoding.GeocodingTestFixtures.otherConfiguration;
import static com.warehouse.geocoding.GeocodingTestFixtures.updateCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.port.secondary.GeocodingRepository;

@ExtendWith(MockitoExtension.class)
class GeocodingServiceImplTest {

    @Mock
    private GeocodingRepository geocodingRepository;

    private GeocodingServiceImpl geocodingService;

    @BeforeEach
    void setUp() {
        geocodingService = new GeocodingServiceImpl(geocodingRepository);
    }

    @Test
    void shouldCreateConfiguration() {
        final GeocodingConfiguration configuration = disabledConfiguration();

        geocodingService.create(configuration);

        verify(geocodingRepository).create(configuration);
        verify(geocodingRepository, never()).findDefault();
    }

    @Test
    void shouldUnsetPreviousDefaultWhenCreatingNewDefaultConfiguration() {
        final GeocodingConfiguration previousDefault = otherConfiguration();
        final GeocodingConfiguration newDefault = configuration();
        when(geocodingRepository.findDefault()).thenReturn(Optional.of(previousDefault));

        geocodingService.create(newDefault);

        assertFalse(previousDefault.isDefaultProvider());
        verify(geocodingRepository).update(previousDefault);
        verify(geocodingRepository).create(newDefault);
    }

    @Test
    void shouldNotUnsetSameConfigurationWhenCreatingDefaultConfigurationWithExistingId() {
        final GeocodingConfiguration newDefault = configuration();
        when(geocodingRepository.findDefault()).thenReturn(Optional.of(newDefault));

        geocodingService.create(newDefault);

        verify(geocodingRepository, never()).update(newDefault);
        verify(geocodingRepository).create(newDefault);
    }

    @Test
    void shouldUpdateConfiguration() {
        final GeocodingConfiguration configuration = configuration();
        when(geocodingRepository.findById(CONFIGURATION_ID)).thenReturn(Optional.of(configuration));

        geocodingService.update(updateCommand());

        assertEquals(GeocodingProvider.POSITION_STACK, configuration.getProvider());
        assertEquals("updated-api-key", configuration.getApiKey());
        verify(geocodingRepository).update(configuration);
    }

    @Test
    void shouldDeleteExistingConfiguration() {
        final GeocodingConfiguration configuration = configuration();
        when(geocodingRepository.findById(CONFIGURATION_ID)).thenReturn(Optional.of(configuration));

        geocodingService.delete(CONFIGURATION_ID);

        verify(geocodingRepository).delete(CONFIGURATION_ID);
    }

    @Test
    void shouldReturnConfigurationById() {
        final GeocodingConfiguration configuration = configuration();
        when(geocodingRepository.findById(CONFIGURATION_ID)).thenReturn(Optional.of(configuration));

        final GeocodingConfiguration result = geocodingService.get(CONFIGURATION_ID);

        assertSame(configuration, result);
    }

    @Test
    void shouldThrowProblemDetailsWhenConfigurationByIdDoesNotExist() {
        when(geocodingRepository.findById(CONFIGURATION_ID)).thenReturn(Optional.empty());

        final ProblemDetailsException exception =
                assertThrows(ProblemDetailsException.class, () -> geocodingService.get(CONFIGURATION_ID));

        assertEquals(404, exception.getStatus());
        assertEquals("Geocoding Configuration Not Found", exception.getTitle());
        assertEquals("Geocoding configuration with id " + CONFIGURATION_ID.value() + " was not found",
                exception.getDetail());
    }

    @Test
    void shouldReturnConfigurationByProvider() {
        final GeocodingConfiguration configuration = configuration();
        when(geocodingRepository.findByProvider(GeocodingProvider.GEOAPIFY)).thenReturn(Optional.of(configuration));

        final GeocodingConfiguration result = geocodingService.getByProvider(GeocodingProvider.GEOAPIFY);

        assertSame(configuration, result);
    }

    @Test
    void shouldThrowProblemDetailsWhenConfigurationByProviderDoesNotExist() {
        when(geocodingRepository.findByProvider(GeocodingProvider.GEOAPIFY)).thenReturn(Optional.empty());

        final ProblemDetailsException exception = assertThrows(ProblemDetailsException.class,
                () -> geocodingService.getByProvider(GeocodingProvider.GEOAPIFY));

        assertEquals(404, exception.getStatus());
        assertEquals("Geocoding configuration for provider GEOAPIFY was not found", exception.getDetail());
    }

    @Test
    void shouldReturnExplicitDefaultConfiguration() {
        final GeocodingConfiguration configuration = configuration();
        when(geocodingRepository.findDefault()).thenReturn(Optional.of(configuration));

        final GeocodingConfiguration result = geocodingService.getDefault();

        assertSame(configuration, result);
        verify(geocodingRepository, never()).findAll();
    }

    @Test
    void shouldFallbackToFirstEnabledConfigurationWhenDefaultDoesNotExist() {
        final GeocodingConfiguration disabledConfiguration = disabledConfiguration();
        final GeocodingConfiguration enabledConfiguration = configuration();
        when(geocodingRepository.findDefault()).thenReturn(Optional.empty());
        when(geocodingRepository.findAll()).thenReturn(List.of(disabledConfiguration, enabledConfiguration));

        final GeocodingConfiguration result = geocodingService.getDefault();

        assertSame(enabledConfiguration, result);
    }

    @Test
    void shouldThrowProblemDetailsWhenDefaultConfigurationDoesNotExist() {
        when(geocodingRepository.findDefault()).thenReturn(Optional.empty());
        when(geocodingRepository.findAll()).thenReturn(List.of(disabledConfiguration()));

        final ProblemDetailsException exception =
                assertThrows(ProblemDetailsException.class, () -> geocodingService.getDefault());

        assertEquals(404, exception.getStatus());
        assertEquals("Default geocoding configuration was not found", exception.getDetail());
    }

    @Test
    void shouldReturnAllConfigurations() {
        final List<GeocodingConfiguration> configurations = List.of(configuration(), otherConfiguration());
        when(geocodingRepository.findAll()).thenReturn(configurations);

        final List<GeocodingConfiguration> result = geocodingService.getAll();

        assertSame(configurations, result);
    }

    @Test
    void shouldNotDeleteMissingConfiguration() {
        when(geocodingRepository.findById(OTHER_CONFIGURATION_ID)).thenReturn(Optional.empty());

        assertThrows(ProblemDetailsException.class, () -> geocodingService.delete(OTHER_CONFIGURATION_ID));

        verify(geocodingRepository, never()).delete(OTHER_CONFIGURATION_ID);
    }
}
