package com.warehouse.delivery.domain.service;

import java.util.List;
import java.util.stream.Stream;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final RouteLogServicePort routeLogServicePort;

    private final DeliveryRepository deliveryRepository;
    @Override
    public List<Delivery> save(List<Delivery> deliveryRequest) {
        final List<Delivery> deliveries = deliveryRequest.stream()
                .map(deliveryRepository::saveDelivery)
                .toList();

        final List<DeliveryResponse> responses = null;

        //routeLogServicePort.deliver(null);

        return deliveries;
    }
}
