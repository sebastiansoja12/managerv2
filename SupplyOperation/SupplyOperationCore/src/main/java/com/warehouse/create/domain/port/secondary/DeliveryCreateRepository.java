package com.warehouse.create.domain.port.secondary;

import com.warehouse.create.domain.model.DeliveryCreate;
import com.warehouse.create.domain.model.Request;

public interface DeliveryCreateRepository {
    DeliveryCreate save(Request request);
}
