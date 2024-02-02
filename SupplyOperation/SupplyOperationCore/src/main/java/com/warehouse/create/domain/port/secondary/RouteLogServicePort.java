package com.warehouse.create.domain.port.secondary;

import com.warehouse.create.domain.model.Request;
import com.warehouse.create.domain.model.Response;

public interface RouteLogServicePort {
    Response logRoute(Request request);
}
