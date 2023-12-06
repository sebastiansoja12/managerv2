package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.routetracker.domain.vo.ParcelId;
import com.warehouse.routetracker.domain.vo.ZebraIdInformation;
import com.warehouse.routetracker.domain.vo.ZebraVersionInformation;
import com.warehouse.routetracker.infrastructure.api.dto.ParcelIdDto;
import com.warehouse.routetracker.infrastructure.api.dto.ZebraIdInformationDto;
import com.warehouse.routetracker.infrastructure.api.dto.ZebraVersionInformationDto;
import org.mapstruct.Mapper;

import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;

@Mapper
public interface RouteRequestMapper {

    RouteRequestDto map(RouteRequest routeRequest);

    List<RouteRequest> map(List<RouteRequestDto> routeRequest);

    RouteRequest map(RouteRequestDto routeRequestDto);

    ParcelId map(ParcelIdDto id);

    ZebraIdInformation map(ZebraIdInformationDto zebraIdInformation);

    ZebraVersionInformation map(ZebraVersionInformationDto versionInformation);
}
