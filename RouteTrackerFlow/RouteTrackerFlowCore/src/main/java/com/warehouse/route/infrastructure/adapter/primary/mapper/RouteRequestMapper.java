package com.warehouse.route.infrastructure.adapter.primary.mapper;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.infrastructure.api.dto.RouteRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RouteRequestMapper {

    RouteRequestDto map(RouteRequest routeRequest);

    RouteRequest map(RouteRequestDto routeRequestDto);
}
