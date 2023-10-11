package com.warehouse.delivery.domain.service;

import java.util.List;

import com.warehouse.delivery.domain.model.Delivery;

public interface DeliveryService {
    List<Delivery> save(List<Delivery> delivery);
}
