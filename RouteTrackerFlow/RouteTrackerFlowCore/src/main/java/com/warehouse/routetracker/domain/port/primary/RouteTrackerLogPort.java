package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.vo.DeliveryInformation;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    void saveDelivery(List<DeliveryInformation> deliveryInformation);

    void deleteRoute(RouteDeleteRequest request);

    List<RouteResponse> saveRoutes(List<RouteRequest> routeRequest);

    List<RouteInformation> getRouteListByParcelId(Long parcelId);

    List<RouteInformation> findRoutesByUsername(String username);
}
