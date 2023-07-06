package com.warehouse.route.domain.port.secondary;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteResponse;

import java.util.UUID;

public interface RouteLogService {

    void initializeRoute(Route route);

    RouteResponse saveSupplyRoute(Route route);

    RouteResponse saveRoute(Route route);

    void deleteRoute(RouteDeleteRequest request);
}
