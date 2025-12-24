package com.warehouse.voronoi.service;

import static com.warehouse.voronoi.service.DepotInMemoryData.depots;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ComputerServiceTest {

    @Mock
    private VoronoiServicePort voronoiServicePort;
    private ComputeServiceImpl computerService;

	@BeforeEach
	void setup() {
		computerService = new ComputeServiceImpl(voronoiServicePort);
	}

    @Test
    void shouldCompute() {
        // given
        final List<Department> depotsList = depots();
        final VoronoiRequest requestCity = new VoronoiRequest("Lublin", null, Collections.emptyList());

        final Coordinates coordinates = Coordinates.builder()
                .lat(50.0)
                .lon(50.0)
                .build();

        doReturn(coordinates)
                .when(voronoiServicePort)
                .obtainCoordinates(requestCity.getCity());
        
        // when
        final DepartmentCode compute = computerService.calculateDepartmentCode(requestCity, depotsList);
        // then nearest depot is LUB
        assertThat(compute.getValue()).isEqualTo("LUB");
    }

    @Test
    void shouldComputeWhenGivenCoordinatesAreSameAsDepots() {
        // given
        final List<Department> depotsList = depots();
        final VoronoiRequest requestCity = new VoronoiRequest("Wroclaw", null, Collections.emptyList());

        final Coordinates coordinates = Coordinates.builder()
                .lon(51.1271647)
                .lat(16.9218245)
                .build();

        doReturn(coordinates)
                .when(voronoiServicePort)
                .obtainCoordinates(requestCity.getCity());

        // when
        final DepartmentCode compute = computerService.calculateDepartmentCode(requestCity, depotsList);
        // then nearest depot is WRO
        assertThat(compute.getValue()).isEqualTo("WRO");
    }

    @Test
    void shouldReturnEmptyDepartmentCode() {
        // given
        final List<Department> depotsList = depots();
        final VoronoiRequest requestCity = new VoronoiRequest("Wroclaw", null, Collections.emptyList());

        doReturn(null)
                .when(voronoiServicePort)
                .obtainCoordinates(requestCity.getCity());

        // when
        final DepartmentCode departmentCode = computerService.calculateDepartmentCode(requestCity, depotsList);
        // then
        assertThat(departmentCode.getValue()).isEmpty();
    }
}
