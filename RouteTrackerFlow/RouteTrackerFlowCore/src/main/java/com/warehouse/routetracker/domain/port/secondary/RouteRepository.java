package com.warehouse.routetracker.domain.port.secondary;

import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.vo.RouteResponse;

import java.util.List;

public interface RouteRepository {

    void initializeRoute(Route route);

    void saveSupplyRoute(SupplyRoute route);

    void deleteRoute(RouteDeleteRequest request);

    List<RouteInformation> findByParcelId(Long parcelId);

    RouteResponse save(Route route);

    List<RouteInformation> findByUsername(String username);
}
