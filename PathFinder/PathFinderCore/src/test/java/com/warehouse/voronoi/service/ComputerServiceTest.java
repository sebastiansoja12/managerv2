package com.warehouse.voronoi.service;

import static com.warehouse.voronoi.service.DepotInMemoryData.depots;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import org.mockito.Mock;

public class ComputerServiceTest {

    @Mock
    private VoronoiServicePort voronoiServicePort;
    private final ComputeService computerService = new ComputeServiceImpl(voronoiServicePort);

    @Test
    void shouldCompute() {
        // given
        final List<Depot> depotsList = depots();
        final String requestCity = "Gliwice";

        // when
        final String compute = computerService.calculate(requestCity, depotsList);
        // then nearest depot is KT1
        assertThat(compute).isEqualTo("KT1");
    }

    @Test
    void shouldComputeWhenGivenCoordinatesAreSameAsDepots() {
        // given
        final List<Depot> depotsList = depots();
        final String requestCity = "Wroclaw";

        // when
        final String compute = computerService.calculate(requestCity, depotsList);
        // then nearest depot is WRO
        assertThat(compute).isEqualTo("WRO");
    }

    @Test
    void shouldThrowException() {
        // given
        final List<Depot> depotsList = new ArrayList<>();

        // when
        final Executable executable = () -> computerService.calculate(null, depotsList);
        // then
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, executable);
        assertThat(exception.getClass()).isInstanceOf(Class.class);
    }

}
