package com.warehouse.routetracker.mapper;

import com.warehouse.routetracker.domain.enumeration.Status;
import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteResponseMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteResponseMapperImpl;
import com.warehouse.routetracker.infrastructure.api.dto.RouteInformationDto;
import com.warehouse.routetracker.infrastructure.api.dto.RouteResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RouteResponseMapperTest {


    private RouteResponseMapper routeResponseMapper;


    @BeforeEach
    void setup() {
        routeResponseMapper = new RouteResponseMapperImpl();
    }

    @Test
    void shouldMapFromResponseToResponseDto() {
        // given
        final RouteResponse response = RouteResponse.builder()
                .id(UUID.randomUUID())
                .build();
        // when
        final RouteResponseDto routeResponseDto = routeResponseMapper.map(response);
        // then
        assertThat(response.getId()).isEqualTo(routeResponseDto.getId());
    }

    @Test
    void shouldMapRouteInformationToDto() {
        // given
        final RouteInformation routeInformation = RouteInformation.builder()
                .status(Status.REROUTE)
                .build();
        // when
        final List<RouteInformationDto> response = routeResponseMapper.map(List.of(routeInformation));
        // then
        assertEquals(routeInformation.getStatus().name(), response.get(0).getStatus().name());
    }
}
