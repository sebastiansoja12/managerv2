package com.warehouse.tsp.mapper;

import com.warehouse.dto.DepotDto;
import com.warehouse.tsp.domain.model.Depot;
import com.warehouse.tsp.infrastructure.adapter.primary.mapper.TSPRequestMapper;
import com.warehouse.tsp.infrastructure.adapter.primary.mapper.TSPRequestMapperImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TSPRequestMapperTest {

    private final TSPRequestMapper tspRequestMapper = new TSPRequestMapperImpl();


    @Test
    void shouldMap() {
        // given
        final DepotDto depotDto = new DepotDto();
        depotDto.setDepotCode("KT1");

        // when
        final Depot depot = tspRequestMapper.map(depotDto);

        // then
        assertThat(depot.getDepotCode()).isEqualTo(depotDto.getDepotCode());
    }

    @Test
    void shouldMapFromList() {
        // given
        final DepotDto depotDto = new DepotDto();
        depotDto.setDepotCode("KT1");

        // and list of depots
        final List<DepotDto> depotDtoList = List.of(depotDto);

        // when
        final List<Depot> depot = tspRequestMapper.map(depotDtoList);

        // then
        assertThat(depot.get(0).getDepotCode()).isEqualTo(depotDtoList.get(0).getDepotCode());
    }
}
