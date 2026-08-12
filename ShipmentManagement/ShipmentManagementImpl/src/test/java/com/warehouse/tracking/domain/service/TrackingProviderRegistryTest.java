package com.warehouse.tracking.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.port.secondary.TrackingProviderServicePort;

class TrackingProviderRegistryTest {

    @Test
    void shouldExposeOnlyAvailableProvidersAndBatchRequestsByTen() {
        final TrackingProviderServicePort provider = mock(TrackingProviderServicePort.class);
        when(provider.providerId()).thenReturn(TrackingProviderId.INPOST);
        when(provider.isAvailable()).thenReturn(true);
        when(provider.track(anyList())).thenReturn(List.of());
        final TrackingProviderRegistry registry = new TrackingProviderRegistry(List.of(provider));
        final List<String> numbers = IntStream.range(0, 21)
                .mapToObj(index -> "TRACK" + String.format("%06d", index))
                .toList();

        assertEquals(List.of(TrackingProviderId.INPOST), registry.availableProviders());
        registry.track(TrackingProviderId.INPOST, numbers);

        verify(provider, times(3)).track(anyList());
    }

    @Test
    void shouldRejectDisabledUnknownProviderAndInvalidTrackingNumber() {
        final TrackingProviderServicePort provider = mock(TrackingProviderServicePort.class);
        when(provider.providerId()).thenReturn(TrackingProviderId.INPOST);
        when(provider.isAvailable()).thenReturn(false);
        final TrackingProviderRegistry registry = new TrackingProviderRegistry(List.of(provider));

        assertThrows(TrackingException.class,
                () -> registry.track(TrackingProviderId.INPOST, List.of("TRACK123456")));
        assertThrows(TrackingException.class,
                () -> registry.track(null, List.of("TRACK123456")));

        when(provider.isAvailable()).thenReturn(true);
        assertThrows(TrackingException.class,
                () -> registry.track(TrackingProviderId.INPOST, List.of("../../invalid")));
        assertThrows(TrackingException.class,
                () -> registry.track(TrackingProviderId.INPOST, null));
    }
}
