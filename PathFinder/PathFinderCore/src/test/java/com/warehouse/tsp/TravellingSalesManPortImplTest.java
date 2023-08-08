package com.warehouse.tsp;

import static com.warehouse.tsp.DepotInMemoryData.depots;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.tsp.domain.exception.MissingDepotsException;
import com.warehouse.tsp.domain.model.Depot;
import com.warehouse.tsp.domain.port.primary.TravellingSalesManPortImpl;
import com.warehouse.tsp.domain.port.secondary.SalesManServicePort;

@ExtendWith(MockitoExtension.class)
public class TravellingSalesManPortImplTest {

    @Mock
    private SalesManServicePort salesManServicePort;
    private TravellingSalesManPortImpl travellingSalesManPort;

    @BeforeEach
    void setup() {
        travellingSalesManPort = new TravellingSalesManPortImpl(salesManServicePort);
    }

    @Test
    void shouldCalculatePath() {
        // given
        final List<Depot> depots = depots();

        final String mockPath = "KT1, KR1, LUB";

        when(salesManServicePort.findFastestRoute(depots)).thenReturn(mockPath);
        // when
        final String path = travellingSalesManPort.findFastestRoute(depots);
        // then
        assertEquals(mockPath, path);
    }


    @Test
    void shouldThrowMissingDepotsException() {
        // given
        final String exceptionMessage = "Depots cannot be empty";
        final List<Depot> emptyDepotList = new ArrayList<>();
        // when
        final Executable executable = () -> travellingSalesManPort.findFastestRoute(emptyDepotList);
        // then
        final MissingDepotsException exception = assertThrows(MissingDepotsException.class, executable);
        assertEquals(exception.getMessage(), exceptionMessage);
    }

}
