package com.warehouse.route.domain.port.primary;

import java.util.List;

import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.vo.DeliveryInformation;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    RouteResponse saveDelivery(List<DeliveryInformation> deliveryInformation);

    RouteResponse saveRoute(RouteRequest routeRequest);

    void saveMultipleRoutes(List<RouteRequest> routeRequests);

    void deleteRoute(RouteDeleteRequest request);
}
