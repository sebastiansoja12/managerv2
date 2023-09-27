package com.warehouse.routetracker.domain.port.secondary;

import com.warehouse.routetracker.domain.model.RouteRequest;
import com.warehouse.routetracker.domain.model.Routes;

import java.util.List;

public interface RouteTrackerServicePort {

    void saveRoute(RouteRequest routeRequest);

    List<Routes> findByParcelId(Long parcelId);

    List<Routes> findByUsername(String username);

    void deleteRoute(Long id);

    boolean exists(Long id);
}
