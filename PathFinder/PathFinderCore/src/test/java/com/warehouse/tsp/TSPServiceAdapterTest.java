package com.warehouse.tsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import com.warehouse.dto.CoordinatesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.dto.DepotDto;
import com.warehouse.tsp.domain.model.Coordinates;
import com.warehouse.tsp.domain.model.Depot;
import com.warehouse.tsp.domain.port.primary.TravellingSalesManPort;
import com.warehouse.tsp.infrastructure.adapter.primary.TSPServiceAdapter;
import com.warehouse.tsp.infrastructure.adapter.primary.mapper.TSPRequestMapper;

@ExtendWith(MockitoExtension.class)
public class TSPServiceAdapterTest {

    @Mock
    private TravellingSalesManPort travellingSalesManPort;

    @Mock
    private TSPRequestMapper requestMapper;

    private TSPServiceAdapter tspServiceAdapter;
    @BeforeEach
    void setup() {
        tspServiceAdapter = new TSPServiceAdapter(requestMapper, travellingSalesManPort);
    }


    @Test
    void shouldCalculatePath() {
        // given
        final List<DepotDto> requestDepots = buildDepots();
        // build expected path
        final String expectedPath = "KR1, KT1, WRO, ZGR";

        // build mapped depots
        final List<Depot> mappedDepots = buildMappedDepots();

        doReturn(mappedDepots)
                .when(requestMapper)
                .map(requestDepots);

        doReturn(expectedPath)
                .when(travellingSalesManPort)
                .findFastestRoute(mappedDepots);
        // when
        final String path = tspServiceAdapter.findFastestRoute(requestDepots);
        // then
        assertEquals(expectedPath, path);
    }

    private List<Depot> buildMappedDepots() {
        final Depot depot1 = Depot.builder()
                .depotCode("KR1")
                .coordinates(Coordinates.builder()
                        .lon(50.0468548)
                        .lat(19.9348336)
                        .build())
                .build();

        final Depot depot2 = Depot.builder()
                .depotCode("KT1")
                .coordinates(Coordinates.builder()
                        .lon(50.3013283)
                        .lat(18.5795769)
                        .build())
                .build();

        return List.of(depot1, depot2);
    }

    private List<DepotDto> buildDepots() {
        final DepotDto depot1 = new DepotDto();
        depot1.setDepotCode("KR1");
        depot1.setCoordinates(CoordinatesDto.builder()
                .lon(50.0468548)
                .lat(19.9348336)
                .build());

        final DepotDto depot2 = new DepotDto();
        depot2.setDepotCode("KT1");
        depot2.setCoordinates(CoordinatesDto.builder()
                .lon(50.3013283)
                .lat(18.5795769)
                .build());

        return List.of(depot1, depot2);
    }
}
