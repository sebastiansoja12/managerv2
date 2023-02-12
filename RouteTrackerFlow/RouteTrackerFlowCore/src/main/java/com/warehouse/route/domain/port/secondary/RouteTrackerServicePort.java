package com.warehouse.route.domain.port.secondary;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;

import java.util.List;

public interface RouteTrackerServicePort {

    RouteResponse saveRoute(RouteRequest routeRequest);

    List<Routes> findByParcelId(Long parcelId);

    List<Routes> findByUsername(String username);

    void deleteRoute(Long id);

    boolean exists(Long id);
}
