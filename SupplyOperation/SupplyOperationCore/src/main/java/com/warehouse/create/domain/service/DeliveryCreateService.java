package com.warehouse.create.domain.service;

import com.warehouse.create.domain.model.Request;
import com.warehouse.create.domain.model.Response;

public interface DeliveryCreateService {
    Response createDelivery(Request request);
}
