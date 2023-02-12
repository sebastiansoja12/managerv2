package com.warehouse.route.domain.port.primary;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.port.secondary.RouteLogService;
import com.warehouse.route.domain.port.secondary.RouteRepository;
import com.warehouse.route.domain.port.secondary.RouteTrackerServicePort;
import com.warehouse.route.domain.vo.SupplyInformation;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteLogService routeLogService;

    private final RouteTrackerServicePort trackerServicePort;

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
    public List<RouteResponse> saveMultipleRoutes(List<RouteRequest> routeRequests) {
        validateParcels(routeRequests);
        return routeRequests.stream()
                .map(trackerServicePort::saveRoute).toList();
    }

    @Override
    public List<Routes> findByParcelId(Long parcelId) {
        return trackerServicePort.findByParcelId(parcelId);
    }

    @Override
    public List<Routes> findByUsername(String username) {
        return trackerServicePort.findByUsername(username);
    }

    @Override
    public void deleteRoute(Long id) {
        trackerServicePort.deleteRoute(id);
    }

    public void validateParcels(List<RouteRequest> routeRequests) {
        routeRequests
                .forEach(r -> trackerServicePort.exists(r.getParcelId()));
    }
}
