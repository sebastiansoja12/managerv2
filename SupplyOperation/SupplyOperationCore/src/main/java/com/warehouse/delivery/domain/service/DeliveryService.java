package com.warehouse.delivery.domain.service;

import java.util.List;
import java.util.Set;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.DeliveryRequest;

public interface DeliveryService {
    List<Delivery> save(Set<DeliveryRequest> delivery);
}
