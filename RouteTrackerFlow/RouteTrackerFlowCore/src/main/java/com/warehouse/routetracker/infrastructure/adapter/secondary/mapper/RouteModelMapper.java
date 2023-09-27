package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import com.warehouse.routetracker.domain.model.Route;
import com.warehouse.routetracker.domain.model.RouteResponse;
import com.warehouse.routetracker.domain.model.Routes;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteModelMapper {

    List<Route> map(List<RouteEntity> routeEntityList);

    List<Routes> mapToRoutes(List<RouteEntity> routeEntityList);


    RouteEntity map(Route route);

    RouteEntity mapInitialize(Route route);


    RouteResponse mapToRouteResponse(RouteEntity routeEntity);

}
