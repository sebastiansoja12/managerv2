package com.warehouse.voronoi;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.infrastructure.adapter.primary.VoronoiServiceAdapter;

@ExtendWith(MockitoExtension.class)
public class VoronoiServiceAdapterTest {

    @Mock
    private VoronoiPort voronoiPort;

    @InjectMocks
    private VoronoiServiceAdapter adapter;

    @Test
    void shouldFindFastestRoute() {
        // given
        final String requestCity = "Gliwice";
        final String expectedDepotCode = "KT1";

        when(voronoiPort.findFastestRoute(requestCity)).thenReturn(expectedDepotCode);

        // when
        final String actualDepotCode = adapter.findFastestRoute(requestCity);
        // then
        assertEquals(expectedDepotCode, actualDepotCode);
    }
}
