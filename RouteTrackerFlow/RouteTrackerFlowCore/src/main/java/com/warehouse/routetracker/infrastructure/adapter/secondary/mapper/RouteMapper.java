package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import com.warehouse.routetracker.domain.vo.Route;
import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;
import com.warehouse.routetracker.infrastructure.api.dto.RouteResponseDto;

//@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RouteMapper {

    RouteResponseDto map(RouteResponse response);

    RouteRequestDto map(RouteRequest request);

    RouteResponse mapToRouteResponse(RouteRequest routeRequest);

    Route mapToRoute(RouteRequest request);
}
