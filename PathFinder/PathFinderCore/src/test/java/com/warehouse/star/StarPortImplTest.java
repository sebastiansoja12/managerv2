package com.warehouse.star;

import com.warehouse.star.domain.exception.MissingCoordinatesException;
import com.warehouse.star.domain.exception.MissingDepotsException;
import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;
import com.warehouse.star.domain.port.primary.StarPort;
import com.warehouse.star.domain.port.primary.StarPortImpl;
import com.warehouse.star.domain.service.CalculateDistanceBetweenDepots;
import com.warehouse.star.domain.service.CalculateDistanceBetweenDepotsServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.warehouse.star.DepotInMemoryData.buildDepots;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StarPortImplTest {


    private final CalculateDistanceBetweenDepots calculateDistanceBetweenDepots =
            new CalculateDistanceBetweenDepotsServiceImpl();
    private final StarPort starPort = new StarPortImpl(calculateDistanceBetweenDepots);


    @Test
    void shouldCompute() {
        // given
        final String expectedPath = """
                KR1\s
                ↓\s
                CZW\s
                ↓\s
                NCS\s
                ↓\s
                WA1\s
                ↓\s
                CIE\s
                ↓\s
                OLS\s
                ↓\s
                GRU\s
                ↓\s
                GD1""";

        final List<Depot> depotsList = buildDepots();
        // coordinates for Gdańsk
        final Coordinates coordinates = Coordinates.builder()
                .lon(54.3612063)
                .lat(18.5499457)
                .build();
        // when
        final String compute = starPort.starPathFinder("KR1", depotsList, coordinates);
        // then
        assertEquals(compute, expectedPath);
    }

    @Test
    void shouldThrowMissingDepotsException() {
        // given empty arraylist
        final List<Depot> depotsList = new ArrayList<>();
        // coordinates for Gdańsk
        final Coordinates coordinates = Coordinates.builder()
                .lon(54.3612063)
                .lat(18.5499457)
                .build();

        // when && then
        assertThrows(MissingDepotsException.class, () -> starPort.starPathFinder("KR1", depotsList, coordinates));
    }

    @Test
    void shouldThrowMissingCoordinatesException() {
        // given empty arraylist
        final List<Depot> depotsList = buildDepots();
        // empty coordinates
        final Coordinates coordinates = null;
        // when && then
        assertThrows(MissingCoordinatesException.class, () -> starPort.starPathFinder("KR1", depotsList, coordinates));
    }
}
