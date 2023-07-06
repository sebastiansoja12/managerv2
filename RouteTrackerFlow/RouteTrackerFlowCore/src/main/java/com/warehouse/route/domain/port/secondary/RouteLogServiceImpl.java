package com.warehouse.route.domain.port.secondary;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLogServiceImpl implements RouteLogService {

    private final RouteRepository routeRepository;

    @Override
    public void initializeRoute(Route route) {
        routeRepository.initializeRoute(route);
    }

    @Override
    public RouteResponse saveSupplyRoute(Route route) {
        return routeRepository.saveSupplyRoute(route);
    }

    @Override
    public RouteResponse saveRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeRepository.deleteRoute(request);
    }
}
