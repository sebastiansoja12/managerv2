package com.warehouse.voronoi;

import com.warehouse.voronoi.domain.exception.MissingDepotsException;
import com.warehouse.voronoi.domain.exception.MissingRequestCityException;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.warehouse.voronoi.DepotInMemoryData.buildDepots;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class VoronoiPortImplTest {


    @Mock
    private VoronoiServicePort voronoiServicePort;

    private VoronoiPortImpl voronoiPort;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        voronoiPort = new VoronoiPortImpl(voronoiServicePort);
    }

    @Test
    void shouldFindNearestDepot() {
        // given
        final String expectedNearestDepot = "KT1";

        // and: build depots
        final List<Depot> depotsList = buildDepots();

        // request city to send
        final String requestCity = "Gliwice";

        when(voronoiServicePort.findFastestRoute(depotsList, requestCity)).thenReturn(expectedNearestDepot);
        // when
        final String nearestDepot = voronoiPort.findFastestRoute(depotsList, requestCity);
        // then
        assertEquals(nearestDepot, expectedNearestDepot);
    }

    @Test
    void shouldThrowMissingDepotsException() {
        // given empty arraylist
        final List<Depot> depotsList = new ArrayList<>();

        // when && then
        assertThrows(MissingDepotsException.class, () -> {
            voronoiPort.findFastestRoute(depotsList, "KR1");
        });
    }

    @Test
    void shouldThrowMissingRequestCityException() {
        // given empty arraylist
        final List<Depot> depotsList = buildDepots();

        // when && then
        assertThrows(MissingRequestCityException.class, () -> {
            voronoiPort.findFastestRoute(depotsList, null);
        });
    }
}
