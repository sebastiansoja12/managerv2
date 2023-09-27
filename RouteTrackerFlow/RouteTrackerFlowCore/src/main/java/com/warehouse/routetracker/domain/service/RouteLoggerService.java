package com.warehouse.routetracker.domain.service;

import java.util.List;

import com.warehouse.routetracker.domain.model.ParcelStorage;
import com.warehouse.routetracker.domain.model.Route;
import com.warehouse.routetracker.domain.model.RouteDeleteRequest;
import com.warehouse.routetracker.domain.model.RouteResponse;

public interface RouteLoggerService {

    void initializeRoute(Route route);

    RouteResponse saveSupplyRoute(Route route);

    List<ParcelStorage> saveRoute(Route routes);

    void deleteRoute(RouteDeleteRequest request);
}
