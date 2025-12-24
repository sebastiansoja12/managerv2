package com.warehouse.voronoi;

import static com.warehouse.voronoi.DepotInMemoryData.buildDepots;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.DepartmentServicePort;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;

@ExtendWith(MockitoExtension.class)
public class VoronoiPortImplTest {


    @Mock
    private DepartmentServicePort departmentServicePort;

    @Mock
    private VoronoiServicePort voronoiServicePort;

    private VoronoiPortImpl voronoiPort;

    @BeforeEach
    void setup() {
        final ComputeService computeService = new ComputeServiceImpl(voronoiServicePort);
        voronoiPort = new VoronoiPortImpl(departmentServicePort, computeService);
    }

    @Test
    void shouldFindNearestDepot() {
        // given
        final String expectedNearestDepot = "KT1";

        // and: build depots
        final List<Department> depotsList = buildDepots();

        // request city to send
        final String requestCity = "Gliwice";

        doReturn(depotsList)
                .when(departmentServicePort)
                .downloadDepartments();

		doReturn(Coordinates.builder().lon(50.3013283).lat(18.5795769).build())
                .when(voronoiServicePort)
				.obtainCoordinates(requestCity);

        // when
        final VoronoiResponse nearestDepot = voronoiPort.findFastestRoute(new VoronoiRequest(requestCity, null));
        // then
        assertEquals(expectedNearestDepot, nearestDepot.departmentCode().getValue());
    }
}
