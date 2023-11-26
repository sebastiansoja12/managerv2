package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;

@Mapper
public interface RouteRequestMapper {

    RouteRequestDto map(RouteRequest routeRequest);

    List<RouteRequest> map(List<RouteRequestDto> routeRequest);

    RouteRequest map(RouteRequestDto routeRequestDto);
}
