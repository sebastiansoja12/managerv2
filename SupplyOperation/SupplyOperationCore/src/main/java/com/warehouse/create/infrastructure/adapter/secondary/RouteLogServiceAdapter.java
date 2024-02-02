package com.warehouse.create.infrastructure.adapter.secondary;

import com.warehouse.create.domain.model.Request;
import com.warehouse.create.domain.model.Response;
import com.warehouse.create.domain.port.secondary.RouteLogServicePort;
import lombok.AllArgsConstructor;


// TODO
@AllArgsConstructor
public class RouteLogServiceAdapter implements RouteLogServicePort {
    @Override
    public Response logRoute(Request request) {
        return null;
    }
}
