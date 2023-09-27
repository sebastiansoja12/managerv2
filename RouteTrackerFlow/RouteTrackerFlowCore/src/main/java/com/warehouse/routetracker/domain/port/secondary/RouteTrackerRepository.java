package com.warehouse.routetracker.domain.port.secondary;

import com.warehouse.routetracker.domain.model.Route;
import com.warehouse.routetracker.domain.model.RouteDeleteRequest;
import com.warehouse.routetracker.domain.model.RouteResponse;
import com.warehouse.routetracker.domain.model.Routes;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;

import java.util.List;

public interface RouteTrackerRepository {

    List<Routes> findByParcelId(Long parcelId);

    void initializeRoute(Route route);

    RouteResponse saveSupplyRoute(Route route);

    RouteEntity save(Route route);

    List<Routes> findByUsername(String username);

    void deleteRoute(RouteDeleteRequest request);
}
