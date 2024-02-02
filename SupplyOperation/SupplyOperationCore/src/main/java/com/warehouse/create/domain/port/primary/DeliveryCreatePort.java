package com.warehouse.create.domain.port.primary;

import com.warehouse.create.domain.model.Request;
import com.warehouse.create.domain.model.Response;

public interface DeliveryCreatePort {
    Response createDelivery(Request request);
}
