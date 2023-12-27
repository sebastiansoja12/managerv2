package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
import com.warehouse.routetracker.infrastructure.api.dto.*;
import org.mapstruct.Mapper;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import org.mapstruct.Mapping;

@Mapper
public interface RouteResponseMapper {

    RouteResponseDto map(RouteResponse response);

    List<RouteResponseDto> mapToResponse(List<RouteResponse> response);

    List<RouteInformationDto> map(List<RouteInformation> routes);

    @Mapping(target = "status", source = "parcelStatus")
    RouteInformationDto map(RouteInformation routeInformation);

    RouteProcessDto map(RouteProcess routeProcess);

    @Mapping(target = "processId.value", source = "id")
    @Mapping(target = "parcelId.value", source = "parcelId")
    @Mapping(target = "returnCode.value", source = "returnCode")
    @Mapping(target = "faultDescription.value", source = "faultDescription")
    RouteLogRecordDto map(RouteLogRecordToChange routeLogRecordToChange);
}
