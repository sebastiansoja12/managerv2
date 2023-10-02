package com.warehouse.route.mapper;

import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.infrastructure.adapter.primary.mapper.RouteResponseMapper;
import com.warehouse.route.infrastructure.adapter.primary.mapper.RouteResponseMapperImpl;
import com.warehouse.route.infrastructure.api.dto.RouteResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
        final RouteResponse response = new RouteResponse(UUID.randomUUID());
        // when
        final RouteResponseDto routeResponseDto = routeResponseMapper.map(response);
        // then
        assertThat(response.getId()).isEqualTo(routeResponseDto.getId());
    }
}
