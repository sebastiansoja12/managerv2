package com.warehouse.route.domain.port.primary;

import java.util.List;

import com.warehouse.route.domain.model.RouteDeleteRequest;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.vo.DeliveryInformation;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    void saveDelivery(List<DeliveryInformation> deliveryInformation);

    List<RouteResponse> saveRoutes(List<RouteRequest> routeRequest);

    void deleteRoute(RouteDeleteRequest request);

    List<Routes> getRouteListByParcelId(Long parcelId);
}
