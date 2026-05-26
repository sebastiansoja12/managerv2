package com.warehouse.voronoi;

import static com.warehouse.voronoi.DepotInMemoryData.buildDepots;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.GeocodingConfigServicePort;
import com.warehouse.voronoi.domain.port.secondary.GeolocationServiceProvider;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;
import com.warehouse.commonassets.enumeration.GeocodingProvider;

@ExtendWith(MockitoExtension.class)
public class VoronoiPortImplTest {


    @Mock
    private GeolocationServiceProvider geolocationServiceProvider;

    @Mock
    private GeocodingConfigServicePort geocodingConfigServicePort;

    private VoronoiPortImpl voronoiPort;

    @BeforeEach
    void setup() {
        final Set<GeolocationServiceProvider> providers = Set.of(geolocationServiceProvider);
        final GeocodingConfig config =
                new GeocodingConfig(GeocodingProvider.POSITION_STACK, "test", "test", null, null);
        when(geocodingConfigServicePort.findGeocodingConfig(GeocodingProvider.POSITION_STACK))
                .thenReturn(config);
        when(geolocationServiceProvider.canHandle(GeocodingProvider.POSITION_STACK)).thenReturn(true);
        when(geolocationServiceProvider.obtainCoordinates("Gliwice", config))
                .thenReturn(new Coordinates(18.5795769, 50.3013283));
        final ComputeService computeService = new ComputeServiceImpl(providers, geocodingConfigServicePort);
        voronoiPort = new VoronoiPortImpl(computeService, providers, geocodingConfigServicePort);
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
