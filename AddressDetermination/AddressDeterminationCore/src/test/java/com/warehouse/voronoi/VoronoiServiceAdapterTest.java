package com.warehouse.voronoi;

import com.warehouse.depot.api.dto.CoordinatesDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.infrastructure.adapter.primary.VoronoiServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.primary.mapper.AddressRequestMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoronoiServiceAdapterTest {

    @Mock
    private VoronoiPort voronoiPort;

    @Mock
    private AddressRequestMapper addressRequestMapper;

    @InjectMocks
    private VoronoiServiceAdapter adapter;

    @Test
    void shouldFindFastestRoute() {
        // given
        final String requestCity = "Gliwice";
        final String expectedDepotCode = "KT1";


        // build request depot list
        final List<DepotDto> depotRequestList = depotDtoList();


        // build model Depot
        final List<Depot> depotList = new ArrayList<>();
        depotList.add(buildDepot());


        when(addressRequestMapper.map(depotRequestList)).thenReturn(depotList);
        when(adapter.findFastestRoute(depotRequestList, requestCity)).thenReturn(expectedDepotCode);

        // when
        final String actualDepotCode = adapter.findFastestRoute(depotRequestList, requestCity);
        // then
        assertEquals(expectedDepotCode, actualDepotCode);
    }

    List<DepotDto> depotDtoList() {
        final DepotDto depotDto = new DepotDto();
        depotDto.setDepotCode("KT1");
        depotDto.setCoordinates(CoordinatesDto.builder()
                .lat(10)
                .lon(10)
                .build());

        return List.of(depotDto);
    }

    Depot buildDepot() {
        // GLIWICE
        return Depot.builder()
                .depotCode("KT1")
                .coordinates(Coordinates.builder()
                        .lon(50.3013283)
                        .lat(18.5795769)
                        .build())
                .build();
    }
}
