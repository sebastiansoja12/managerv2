package com.warehouse.routetracker.infrastructure.api;


public interface RouteLogEventPublisher {

    void send(RouteLogEvent event);
}
