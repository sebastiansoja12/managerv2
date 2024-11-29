package com.warehouse.deliverymissed.domain.port.primary;


import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryMissedPortImpl implements DeliveryMissedPort {

    private final DeliveryMissedService deliveryMissedService;

    private final RouteLogMissedServicePort logMissedServicePort;

    @Override
    public DeliveryMissedResponse logMissedDelivery(final DeliveryMissedRequest request) {
        return DeliveryMissedResponse.builder().build();
    }
}
