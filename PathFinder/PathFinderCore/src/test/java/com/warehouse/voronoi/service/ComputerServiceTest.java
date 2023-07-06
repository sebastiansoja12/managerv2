package com.warehouse.voronoi.service;

import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.warehouse.voronoi.service.DepotInMemoryData.depots;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComputerServiceTest {

    private final ComputeService computerService = new ComputeServiceImpl();

    @Test
    void shouldCompute() {
        // given
        final List<Depot> depotsList = depots();
        final Coordinates coordinates = Coordinates.builder()
                .lon(50.1097081)
                .lat(18.4792279)
                .build();

        // when
        final String compute = computerService.computeLength(coordinates, depotsList);
        // then nearest depot is KT1
        assertThat(compute).isEqualTo("KT1");
    }

    @Test
    void shouldComputeWhenGivenCoordinatesAreSameAsDepots() {
        // given
        final List<Depot> depotsList = depots();

        // Wroclaws coordinates
        final Coordinates coordinates = Coordinates.builder()
                .lon(51.1271647)
                .lat(16.9218245)
                .build();

        // when
        final String compute = computerService.computeLength(coordinates, depotsList);
        // then nearest depot is WRO
        assertThat(compute).isEqualTo("WRO");
    }

    @Test
    void shouldThrowException() {
        // given
        final List<Depot> depotsList = new ArrayList<>();
        final Coordinates coordinates = Coordinates.builder()
                .build();
        // when
        final Executable executable = () -> computerService.computeLength(coordinates, depotsList);
        // then
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, executable);
        assertThat(exception.getClass()).isInstanceOf(Class.class);
    }

}
