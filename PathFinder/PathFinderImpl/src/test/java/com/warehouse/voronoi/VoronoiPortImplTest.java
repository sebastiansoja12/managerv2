package com.warehouse.voronoi;

import static com.warehouse.voronoi.DepotInMemoryData.buildDepots;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;

import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.application.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;

public class VoronoiPortImplTest {

    @Test
    void shouldFindNearestDepot() {
        // given
        final String expectedNearestDepot = "KT1";

        // and: build depots
        final List<Department> depotsList = buildDepots();

        // request city to send
        final String requestCity = "Gliwice";

        // when
        final ComputeService computeService = new ComputeServiceImpl();
        final VoronoiPortImpl voronoiPort = new VoronoiPortImpl(computeService);
        final VoronoiResponse nearestDepot = voronoiPort.findFastestRoute(new VoronoiRequest(requestCity,
                new Coordinates(18.5795769, 50.3013283), depotsList));
        // then
        assertEquals(expectedNearestDepot, nearestDepot.departmentCode().getValue());
    }
}
