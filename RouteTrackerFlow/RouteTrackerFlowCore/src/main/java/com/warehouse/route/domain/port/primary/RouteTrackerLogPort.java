package com.warehouse.route.domain.port.primary;

import java.util.List;

import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.vo.SupplyInformation;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    RouteResponse saveSupplyRoute(List<SupplyInformation> supplyInformation);

    RouteResponse saveRoute(RouteRequest routeRequest);

    void saveMultipleRoutes(List<RouteRequest> routeRequests);

    void deleteRoute(RouteDeleteRequest request);
}
