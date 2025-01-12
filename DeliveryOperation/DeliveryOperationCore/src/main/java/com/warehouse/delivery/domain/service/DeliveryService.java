package com.warehouse.delivery.domain.service;

import java.util.Set;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;

public interface DeliveryService {
    Set<DeliveryResponse> save(final Set<DeliveryRequest> delivery);
}
