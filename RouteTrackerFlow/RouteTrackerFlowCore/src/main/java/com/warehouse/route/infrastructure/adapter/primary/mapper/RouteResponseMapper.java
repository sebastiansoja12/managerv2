package com.warehouse.route.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.route.domain.model.Routes;
import com.warehouse.tracker.infrastructure.api.dto.RoutesDto;
import org.mapstruct.Mapper;

import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.infrastructure.api.dto.RouteResponseDto;

@Mapper
public interface RouteResponseMapper {

    RouteResponseDto map(RouteResponse response);

    List<RouteResponseDto> mapToResponse(List<RouteResponse> response);

    List<RoutesDto> map(List<Routes> routes);

    RoutesDto map(Routes routes);
}
