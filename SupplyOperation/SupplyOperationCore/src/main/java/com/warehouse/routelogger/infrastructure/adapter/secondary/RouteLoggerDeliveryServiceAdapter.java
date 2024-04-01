package com.warehouse.routelogger.infrastructure.adapter.secondary;

import org.springframework.web.client.RestClient;

import com.warehouse.routelogger.domain.port.secondary.RouteLoggerDeliveryServicePort;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

public class RouteLoggerDeliveryServiceAdapter implements RouteLoggerDeliveryServicePort {

    private final RestClient restClient;

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    public RouteLoggerDeliveryServiceAdapter(RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
    }

    @Override
    public void logAnyDelivery(Object o) {
        //
    }
}
