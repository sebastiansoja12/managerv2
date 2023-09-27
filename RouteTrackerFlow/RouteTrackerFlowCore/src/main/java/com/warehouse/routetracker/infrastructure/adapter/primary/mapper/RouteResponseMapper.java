package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import com.warehouse.routetracker.domain.model.RouteResponse;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.RouteResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RouteResponseMapper {

    RouteResponse map(RouteResponseDto routeResponseDto);

    RouteResponseDto map(RouteResponse response);

    List<RouteResponseDto> map(List<RouteResponse> responses);
}
