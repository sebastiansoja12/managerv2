package com.warehouse.route.domain.port.primary;

import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.vo.SupplyInformation;

import java.util.List;
import java.util.UUID;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    RouteResponse saveSupplyRoute(SupplyInformation supplyInformation);

    RouteResponse saveRoute(RouteRequest routeRequest);

    void saveMultipleRoutes(List<RouteRequest> routeRequests);

    void deleteRoute(RouteDeleteRequest request);
}
