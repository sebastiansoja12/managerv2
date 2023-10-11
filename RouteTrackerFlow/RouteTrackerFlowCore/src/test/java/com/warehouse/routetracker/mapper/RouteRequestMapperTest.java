package com.warehouse.routetracker.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.RouteRequestMapper;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;

@ExtendWith(MockitoExtension.class)
public class RouteRequestMapperTest {

    private final RouteRequestMapper routeRequestMapper = getMapper(RouteRequestMapper.class);
    
    @Test
    void shouldMapFromRequestToRequestDto() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .supplierCode("supplierCode")
                .build();
        // when
        final RouteRequestDto routeRequestDto = routeRequestMapper.map(routeRequest);
        // then
        assertEquals(routeRequest.getSupplierCode(), routeRequestDto.getSupplierCode());
    }

    @Test
    void shouldMapFromRequestDtoToRequest() {
        // given
        final RouteRequestDto routeRequestDto = RouteRequestDto.builder()
                .supplierCode("supplierCode")
                .build();
        // when
        final RouteRequest routeRequest = routeRequestMapper.map(routeRequestDto);
        // then
        assertEquals(routeRequestDto.getSupplierCode(), routeRequest.getSupplierCode());
    }
}
