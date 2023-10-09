package com.warehouse.routetracker.mapper;

import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteRequestMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteRequestMapperImpl;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RouteRequestMapperTest {

    private RouteRequestMapper routeRequestMapper;


    @BeforeEach
    void setup() {
        routeRequestMapper = new RouteRequestMapperImpl();
    }


    @Test
    void shouldMapFromRequestToRequestDto() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .supplierCode("supplierCode")
                .build();
        // when
        final RouteRequestDto routeRequestDto = routeRequestMapper.map(routeRequest);
        // then
        assertThat(routeRequest.getSupplierCode()).isEqualTo(routeRequestDto.getSupplierId());
    }

    @Test
    void shouldMapFromRequestDtoToRequest() {
        // given
        final RouteRequestDto routeRequestDto = RouteRequestDto.builder()
                .supplierId(1L)
                .build();
        // when
        final RouteRequest routeRequest = routeRequestMapper.map(routeRequestDto);
        // then
        assertThat(routeRequest.getSupplierCode()).isEqualTo(routeRequestDto.getSupplierId());
    }
}
