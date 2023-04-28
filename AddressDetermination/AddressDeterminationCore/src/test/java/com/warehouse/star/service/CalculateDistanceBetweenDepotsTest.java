package com.warehouse.star.service;

import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;
import com.warehouse.star.domain.service.CalculateDistanceBetweenDepots;
import com.warehouse.star.domain.service.CalculateDistanceBetweenDepotsServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculateDistanceBetweenDepotsTest {
    private final CalculateDistanceBetweenDepots calculateDistanceBetweenDepots =
            new CalculateDistanceBetweenDepotsServiceImpl();

    @Test
    void shouldCalculateDistance() {
        // given
        final double expectedDistanceBetweenDepots = 153.04679741751193;

        // GLIWICE
        final Depot depotKT1 = Depot.builder()
                .depotCode("KT1")
                .coordinates(Coordinates.builder()
                        .lon(50.3013283)
                        .lat(18.5795769)
                        .build())
                .build();

        // KRAKÓW
        final Depot depotKR1 = Depot.builder()
                .depotCode("KR1")
                .coordinates(Coordinates.builder()
                        .lon(50.0468548)
                        .lat(19.9348336)
                        .build())
                .build();

        // when
        final double distance =
                calculateDistanceBetweenDepots.calculateDistanceBetweenDepots(depotKT1, depotKR1);
        // then
        assertThat(distance).isEqualTo(expectedDistanceBetweenDepots);
    }

    @Test
    void shouldCalculateDistanceWithoutEarthCurve() {
        // given
        final double expectedDistanceBetweenDepots = 100.98804411276734;

        // GLIWICE
        final Depot depotKT1 = Depot.builder()
                .depotCode("KT1")
                .coordinates(Coordinates.builder()
                        .lon(50.3013283)
                        .lat(18.5795769)
                        .build())
                .build();

        // KRAKÓW
        final Depot depotKR1 = Depot.builder()
                .depotCode("KR1")
                .coordinates(Coordinates.builder()
                        .lon(50.0468548)
                        .lat(19.9348336)
                        .build())
                .build();

        // when
        final double distance =
                calculateDistanceBetweenDepots.calculateDistanceBetweenDepotsWithoutEarthCurve(depotKT1, depotKR1);
        // then
        assertThat(distance).isEqualTo(expectedDistanceBetweenDepots);
    }
}
