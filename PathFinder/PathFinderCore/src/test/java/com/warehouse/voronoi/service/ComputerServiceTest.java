package com.warehouse.voronoi.service;

import static com.warehouse.voronoi.service.DepotInMemoryData.depots;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.voronoi.domain.exception.MissingCoordinatesException;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Depot;
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
        final List<Depot> depotsList = depots();
        final String requestCity = "Lublin";

        final Coordinates coordinates = Coordinates.builder()
                .lat(50)
                .lon(50)
                .build();

        doReturn(coordinates)
                .when(voronoiServicePort)
                .obtainCoordinates(requestCity);
        
        // when
        final String compute = computerService.calculate(requestCity, depotsList);
        // then nearest depot is LUB
        assertThat(compute).isEqualTo("LUB");
    }

    @Test
    void shouldComputeWhenGivenCoordinatesAreSameAsDepots() {
        // given
        final List<Depot> depotsList = depots();
        final String requestCity = "Wroclaw";

        final Coordinates coordinates = Coordinates.builder()
                .lon(51.1271647)
                .lat(16.9218245)
                .build();

        doReturn(coordinates)
                .when(voronoiServicePort)
                .obtainCoordinates(requestCity);

        // when
        final String compute = computerService.calculate(requestCity, depotsList);
        // then nearest depot is WRO
        assertThat(compute).isEqualTo("WRO");
    }

    @Test
    void shouldThrowException() {
        // given
        final List<Depot> depotsList = depots();
        final String requestCity = "Wroclaw";

        doReturn(null)
                .when(voronoiServicePort)
                .obtainCoordinates(requestCity);

        // when
        final Executable executable = () -> computerService.calculate(requestCity, depotsList);
        // then
        final MissingCoordinatesException exception = assertThrows(MissingCoordinatesException.class, executable);
        assertEquals(expectedToBe("Coordinates missing"), exception.getMessage());
    }

    private <T> T expectedToBe(T s) {
        return s;
    }

}
