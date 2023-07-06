package com.warehouse.route.domain.port.secondary;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;

import java.util.List;

public interface RouteRepository {

    List<Routes> findByParcelId(Long parcelId);

    void initializeRoute(Route route);

    RouteResponse saveSupplyRoute(Route route);

    RouteResponse save(Route route);

    List<Routes> findByUsername(String username);

    void deleteRoute(RouteDeleteRequest request);
}
