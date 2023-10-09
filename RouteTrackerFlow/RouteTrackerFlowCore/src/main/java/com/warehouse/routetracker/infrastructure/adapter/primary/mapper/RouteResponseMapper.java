package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.infrastructure.api.dto.RouteInformationDto;
import org.mapstruct.Mapper;

import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.api.dto.RouteResponseDto;

@Mapper
public interface RouteResponseMapper {

    RouteResponseDto map(RouteResponse response);

    List<RouteResponseDto> mapToResponse(List<RouteResponse> response);

    List<RouteInformationDto> map(List<RouteInformation> routes);

    RouteInformationDto map(RouteInformation routeInformation);
}
