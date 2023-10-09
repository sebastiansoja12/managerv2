package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RouteRequestMapper {

    RouteRequestDto map(RouteRequest routeRequest);

    List<RouteRequest> map(List<RouteRequestDto> routeRequest);

    RouteRequest map(RouteRequestDto routeRequestDto);
}
