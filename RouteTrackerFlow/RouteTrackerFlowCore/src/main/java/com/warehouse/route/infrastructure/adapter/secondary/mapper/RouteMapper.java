package com.warehouse.route.infrastructure.adapter.secondary.mapper;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.infrastructure.api.dto.RouteRequestDto;
import com.warehouse.route.infrastructure.api.dto.RouteResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface RouteMapper {

    RouteResponseDto map(RouteResponse response);

    RouteRequestDto map(RouteRequest request);

    RouteResponse mapToRouteResponse(RouteRequest routeRequest);
}
