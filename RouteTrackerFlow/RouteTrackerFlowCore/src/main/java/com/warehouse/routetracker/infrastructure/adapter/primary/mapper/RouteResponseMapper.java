package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.api.dto.RouteInformationDto;
import com.warehouse.routetracker.infrastructure.api.dto.RouteProcessDto;
import com.warehouse.routetracker.infrastructure.api.dto.RouteResponseDto;
import org.mapstruct.Mapping;

@Mapper
public interface RouteResponseMapper {

    RouteResponseDto map(RouteResponse response);

    List<RouteResponseDto> mapToResponse(List<RouteResponse> response);

    List<RouteInformationDto> map(List<RouteInformation> routes);

    @Mapping(target = "status", source = "parcelStatus")
    RouteInformationDto map(RouteInformation routeInformation);

    RouteProcessDto map(RouteProcess routeProcess);
}
