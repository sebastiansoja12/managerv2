package com.warehouse.route.domain.port.primary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.warehouse.route.domain.model.*;
import com.warehouse.route.domain.port.secondary.RouteLogService;
import com.warehouse.route.domain.vo.SupplyInformation;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteLogService routeLogService;

    @Override
    public void initializeRoute(Long parcelId) {
        final Route route = Route.builder()
                .parcelId(parcelId)
                .created(LocalDateTime.now())
                .build();
        routeLogService.initializeRoute(route);
    }

    @Override
    public RouteResponse saveSupplyRoute(SupplyInformation supplyInformation) {
        final Route route = Route.builder()
                .parcelId(supplyInformation.getParcelId())
                .created(supplyInformation.created())
                .supplierId(supplyInformation.getSupplierId())
                .build();

        return routeLogService.saveSupplyRoute(route);
    }

    @Override
    public RouteResponse saveRoute(RouteRequest routeRequest) {
        final Route route = Route.builder()
                .parcelId(routeRequest.getParcelId())
                .created(LocalDateTime.now())
                .supplierId(routeRequest.getSupplierId())
                .depotId(routeRequest.getDepotId())
                .userId(routeRequest.getUserId())
                .build();
        return routeLogService.saveRoute(route);
    }

    @Override
    public void saveMultipleRoutes(List<RouteRequest> routeRequests) {
        routeRequests.stream()
                .map(this::mapToRoute)
                .forEach(routeLogService::saveRoute);
    }
    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeLogService.deleteRoute(request);
    }

    private Route mapToRoute(RouteRequest routeRequest) {
        return Route.builder()
                .parcelId(routeRequest.getParcelId())
                .created(LocalDateTime.now())
                .supplierId(routeRequest.getSupplierId())
                .depotId(routeRequest.getDepotId())
                .userId(routeRequest.getUserId())
                .build();
    }
}
