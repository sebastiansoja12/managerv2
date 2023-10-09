package com.warehouse.routetracker.domain.port.secondary;

import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.vo.RouteResponse;

import java.util.List;

public interface RouteRepository {

    List<RouteInformation> findByParcelId(Long parcelId);

    void initializeRoute(Route route);

    RouteResponse saveSupplyRoute(SupplyRoute route);

    RouteResponse save(Route route);

    List<RouteInformation> findByUsername(String username);

    void deleteRoute(RouteDeleteRequest request);
}
