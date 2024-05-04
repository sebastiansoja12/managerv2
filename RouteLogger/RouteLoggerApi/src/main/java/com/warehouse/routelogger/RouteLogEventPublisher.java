package com.warehouse.routelogger;

public interface RouteLogEventPublisher {
    void send(RouteLogEvent routeLogEvent);
}
