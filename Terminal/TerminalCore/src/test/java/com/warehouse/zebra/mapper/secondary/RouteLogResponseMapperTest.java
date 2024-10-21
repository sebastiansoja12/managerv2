package com.warehouse.zebra.mapper.secondary;

import com.warehouse.zebra.domain.vo.RouteProcess;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.RouteProcessDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.RouteLogResponseMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

public class RouteLogResponseMapperTest {

    private final RouteLogResponseMapper mapper = getMapper(RouteLogResponseMapper.class);

    @Test
    void shouldMapToRouteProcess() {
        // given
        final RouteProcessDto process = RouteProcessDto.builder()
                .parcelId(1L)
                .build();
        // when
        final RouteProcess routeProcess = mapper.map(process);
        // then
        assertEquals(1L, routeProcess.getParcelId());
    }
}
