package com.warehouse.star;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.dto.CoordinatesDto;
import com.warehouse.dto.DepotDto;
import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;
import com.warehouse.star.domain.port.primary.StarPort;
import com.warehouse.star.infrastructure.adapter.primary.StarServiceAdapter;
import com.warehouse.star.infrastructure.adapter.primary.mapper.StarRequestMapper;


@ExtendWith(MockitoExtension.class)
public class StarServiceAdapterTest {

    @Mock
    StarRequestMapper starRequestMapper;

    @Mock
    StarPort starPort;

    private StarServiceAdapter starServiceAdapter;

    @BeforeEach
    void setUp() {
        starServiceAdapter = new StarServiceAdapter(starPort, starRequestMapper);
    }

    @Test
    void shouldFindPath() {
        // given
        final List<DepotDto> depotsList = buildDepots();
        final List<Depot> depotMappedList = buildMappedDepots();

        // create coordinates DTO
        final CoordinatesDto coordinates = CoordinatesDto.builder()
                .lon(54.3612063)
                .lat(18.5499457)
                .build();
        // create coordinates after mapping
        final Coordinates mappedCoordinates = Coordinates.builder()
                .lon(54.3612063)
                .lat(18.5499457)
                .build();

        when(starRequestMapper.map(depotsList)).thenReturn(depotMappedList);
        when(starRequestMapper.map(coordinates)).thenReturn(mappedCoordinates);
        when(starPort.starPathFinder("KR1", depotMappedList, mappedCoordinates)).thenReturn("KR1 -> KT1");
        // when
        final String compute = starServiceAdapter.starPathFinder("KR1", depotsList, coordinates);
        // then
        assertThat(compute).isEqualTo("KR1 -> KT1");
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


        final DepotDto depot2 = new DepotDto();
        depot2.setDepotCode("KT1");

        return List.of(depot1, depot2);
    }
}
