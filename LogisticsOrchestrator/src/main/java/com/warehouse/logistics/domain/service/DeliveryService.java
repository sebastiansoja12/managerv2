package com.warehouse.logistics.domain.service;

import java.util.Set;

import com.warehouse.logistics.domain.model.DeliveryRequest;
import com.warehouse.logistics.domain.model.DeliveryResponse;

public interface DeliveryService {
    Set<DeliveryResponse> save(final Set<DeliveryRequest> delivery);
}
