package com.warehouse.routetracker.infrastructure.adapter.secondary.exception;

import com.warehouse.routetracker.configuration.common.RestException;

public class RouteLogException extends RestException {

    private final static int code = 403;

    public RouteLogException(String exMessage) {
        super(code, exMessage);
    }
}
