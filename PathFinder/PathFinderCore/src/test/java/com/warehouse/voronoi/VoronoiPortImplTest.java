package com.warehouse.voronoi;

import static com.warehouse.voronoi.DepotInMemoryData.buildDepots;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.GeocodingConfigServicePort;
import com.warehouse.voronoi.domain.port.secondary.GeolocationServiceProvider;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;

@ExtendWith(MockitoExtension.class)
public class VoronoiPortImplTest {


    @Mock
    private Set<GeolocationServiceProvider> geolocationServiceProvider;

    @Mock
    private GeocodingConfigServicePort geocodingConfigServicePort;

    private VoronoiPortImpl voronoiPort;

    @BeforeEach
    void setup() {
        final ComputeService computeService = new ComputeServiceImpl(geolocationServiceProvider, geocodingConfigServicePort);
        voronoiPort = new VoronoiPortImpl(computeService, geolocationServiceProvider, geocodingConfigServicePort);
    }

    @Test
    void shouldFindNearestDepot() {
        // given
        final String expectedNearestDepot = "KT1";

        // and: build depots
        final List<Department> depotsList = buildDepots();

        // request city to send
        final String requestCity = "Gliwice";

        // when
        final VoronoiResponse nearestDepot = voronoiPort.findFastestRoute(new VoronoiRequest(requestCity, null,
                depotsList));
        // then
        assertEquals(expectedNearestDepot, nearestDepot.departmentCode().getValue());
    }
}
