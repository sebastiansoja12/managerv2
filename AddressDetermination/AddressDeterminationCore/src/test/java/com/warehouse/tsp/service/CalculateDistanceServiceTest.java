package com.warehouse.tsp.service;

import com.warehouse.tsp.domain.model.Coordinates;
import com.warehouse.tsp.domain.model.Depot;
import com.warehouse.tsp.domain.service.CalculateDistanceBetweenDepots;
import com.warehouse.tsp.domain.service.CalculateDistanceBetweenDepotsServiceImpl;
import com.warehouse.tsp.domain.service.CalculateDistanceService;
import com.warehouse.tsp.domain.service.CalculateDistanceServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.warehouse.tsp.service.DepotInMemoryData.depots;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CalculateDistanceServiceTest {

    private final CalculateDistanceBetweenDepots calculateDistanceBetweenDepots =
            new CalculateDistanceBetweenDepotsServiceImpl();

    private final CalculateDistanceService calculateDistanceService =
            new CalculateDistanceServiceImpl(calculateDistanceBetweenDepots);

    @Test
    void shouldCalculateDistance() {
        // given
        final List<Depot> depots = depots();

        // and: routes values
        final List<Integer> route = Arrays.asList(0, 1, 2, 3, 4, 5);

        // when
        final double distance = calculateDistanceService.calculateDistance(route, depots);
        // then
        assertThat(distance).isEqualTo(2194.483332914455);
    }
}
