package com.warehouse.tsp;

import com.warehouse.tsp.domain.exception.MissingDepotsException;
import com.warehouse.tsp.domain.model.Depot;
import com.warehouse.tsp.domain.port.primary.TravellingSalesManPort;
import com.warehouse.tsp.domain.port.primary.TravellingSalesManPortImpl;
import com.warehouse.tsp.domain.port.secondary.SalesManServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TravellingSalesManPortImplTest {

    @Mock
    private SalesManServicePort salesManServicePort;
    private final TravellingSalesManPort travellingSalesManPort = new TravellingSalesManPortImpl(salesManServicePort);


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
