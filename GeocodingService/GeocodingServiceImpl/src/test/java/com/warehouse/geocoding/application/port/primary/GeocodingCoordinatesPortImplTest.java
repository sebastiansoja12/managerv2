package com.warehouse.geocoding.application.port.primary;

import static com.warehouse.geocoding.GeocodingTestFixtures.configuration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.application.port.secondary.GeocodingAddressRepository;
import com.warehouse.geocoding.application.port.secondary.GeocodingProviderServicePort;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;

@ExtendWith(MockitoExtension.class)
class GeocodingCoordinatesPortImplTest {

    private static final GeocodingAddress ADDRESS = new GeocodingAddress("Katowice", "Mariacka 12", "40-001", "PL");
    private static final Coordinates COORDINATES = new Coordinates(50.26, 19.02);

    @Mock
    private GeocodingPort geocodingPort;

    @Mock
    private GeocodingProviderServicePort provider;

    @Mock
    private GeocodingAddressRepository geocodingAddressRepository;

    private GeocodingCoordinatesPortImpl port;

    @BeforeEach
    void setUp() {
        port = new GeocodingCoordinatesPortImpl(geocodingPort, Set.of(provider), geocodingAddressRepository);
        lenient().when(geocodingPort.getDefault()).thenReturn(configuration());
        lenient().when(provider.canHandle(GeocodingProvider.GEOAPIFY)).thenReturn(true);
    }

    @Test
    void shouldCallProviderAndCacheResultWhenAddressIsUnknown() {
        when(geocodingAddressRepository.findCoordinates(ADDRESS.normalize())).thenReturn(Optional.empty());
        when(provider.obtainCoordinates(any(), any())).thenReturn(COORDINATES);

        assertSame(COORDINATES, port.obtainCoordinates(ADDRESS));

        verify(geocodingAddressRepository).saveIfAbsent(ADDRESS.normalize(), COORDINATES, GeocodingProvider.GEOAPIFY);
    }

    @Test
    void shouldReturnCachedCoordinatesWithoutCallingProvider() {
        when(geocodingAddressRepository.findCoordinates(ADDRESS.normalize())).thenReturn(Optional.of(COORDINATES));

        assertEquals(COORDINATES, port.obtainCoordinates(ADDRESS));

        verifyNoInteractions(provider, geocodingPort);
        verify(geocodingAddressRepository, never()).saveIfAbsent(any(), any(), any());
    }

    @Test
    void shouldNotCacheWhenProviderReturnsNoCoordinates() {
        when(geocodingAddressRepository.findCoordinates(any())).thenReturn(Optional.empty());
        when(provider.obtainCoordinates(any(), any())).thenReturn(new Coordinates(null, null));

        final Coordinates result = port.obtainCoordinates(ADDRESS);

        assertNull(result.lat());
        verify(geocodingAddressRepository, never()).saveIfAbsent(any(), any(), any());
    }

    @Test
    void shouldNotCacheApproximateCoordinates() {
        final Coordinates approximate = new Coordinates(50.139048, 18.602265, true);
        when(geocodingAddressRepository.findCoordinates(any())).thenReturn(Optional.empty());
        when(provider.obtainCoordinates(any(), any())).thenReturn(approximate);

        assertSame(approximate, port.obtainCoordinates(ADDRESS));

        verify(geocodingAddressRepository, never()).saveIfAbsent(any(), any(), any());
    }

    @Test
    void shouldPropagateProviderErrorWithoutCaching() {
        final IllegalStateException failure = new IllegalStateException("provider down");
        when(geocodingAddressRepository.findCoordinates(any())).thenReturn(Optional.empty());
        when(provider.obtainCoordinates(any(), any())).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class, () -> port.obtainCoordinates(ADDRESS)));

        verify(geocodingAddressRepository, never()).saveIfAbsent(any(), any(), any());
    }
}
