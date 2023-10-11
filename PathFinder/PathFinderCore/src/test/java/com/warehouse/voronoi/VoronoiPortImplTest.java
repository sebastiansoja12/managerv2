package com.warehouse.voronoi;

import static com.warehouse.voronoi.DepotInMemoryData.buildDepots;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.voronoi.domain.exception.MissingDepotsException;
import com.warehouse.voronoi.domain.exception.MissingRequestCityException;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.DepotServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;

@ExtendWith(MockitoExtension.class)
public class VoronoiPortImplTest {


    @Mock
    private DepotServicePort depotServicePort;

    @Mock
    private ComputeService computeService;

    private VoronoiPortImpl voronoiPort;

    @BeforeEach
    void setup() {
        voronoiPort = new VoronoiPortImpl(depotServicePort, computeService);
    }

    @Test
    void shouldFindNearestDepot() {
        // given
        final String expectedNearestDepot = "KT1";

        // and: build depots
        final List<Depot> depotsList = buildDepots();

        // request city to send
        final String requestCity = "Gliwice";

        doReturn(depotsList)
                .when(depotServicePort)
                .downloadDepots();

        doReturn("KT1")
                .when(computeService)
                .calculate(requestCity, depotsList);

        // when
        final String nearestDepot = voronoiPort.findFastestRoute(requestCity);
        // then
        assertEquals(expectedNearestDepot, nearestDepot);
    }

    @Test
    void shouldThrowMissingDepotsException() {
        // given empty arraylist
        final List<Depot> depotsList = new ArrayList<>();

        doReturn(depotsList)
                .when(depotServicePort)
                .downloadDepots();

        // when && then
        assertThrows(MissingDepotsException.class, () -> {
            voronoiPort.findFastestRoute("KR1");
        });
    }

    @Test
    void shouldThrowMissingRequestCityException() {
        // given empty arraylist
        final List<Depot> depotsList = buildDepots();

        doReturn(depotsList)
                .when(depotServicePort)
                .downloadDepots();

        // when && then
        assertThrows(MissingRequestCityException.class, () -> {
            voronoiPort.findFastestRoute(null);
        });
    }
}
