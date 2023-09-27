package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.routetracker.domain.model.RouteDeleteRequest;
import com.warehouse.routetracker.domain.model.RouteRequest;
import com.warehouse.routetracker.domain.model.RouteResponse;
import com.warehouse.routetracker.domain.vo.SupplyInformation;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    RouteResponse saveSupplyRoute(List<SupplyInformation> supplyInformation);

    List<RouteResponse> saveMultipleRoutes(List<RouteRequest> routeRequests);

    void deleteRoute(RouteDeleteRequest request);
}
