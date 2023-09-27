package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import com.warehouse.routetracker.domain.model.RouteRequest;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.RouteRequestDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RouteRequestMapper {

    RouteRequestDto map(RouteRequest routeRequest);

    RouteRequest map(RouteRequestDto routeRequestDto);

    List<RouteRequest> map(List<RouteRequestDto> routeRequests);
}
