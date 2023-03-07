package com.warehouse.route.domain.port.primary;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.vo.SupplyInformation;

import java.util.List;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    RouteResponse saveSupplyRoute(SupplyInformation supplyInformation);

    RouteResponse saveRoute(RouteRequest routeRequest);

    void saveMultipleRoutes(List<RouteRequest> routeRequests);

    List<Routes> findByParcelId(Long parcelId);

    List<Routes> findByUsername(String username);

    void deleteRoute(Long id);
}
