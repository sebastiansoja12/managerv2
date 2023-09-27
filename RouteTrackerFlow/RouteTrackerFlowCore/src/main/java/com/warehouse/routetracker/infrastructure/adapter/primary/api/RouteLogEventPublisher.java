package com.warehouse.routetracker.infrastructure.adapter.primary.api;


public interface RouteLogEventPublisher {

    void send(RouteLogEvent event);
}
