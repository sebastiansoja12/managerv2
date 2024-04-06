package com.warehouse.deliverymissed.domain.service;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryMissedServiceImpl implements DeliveryMissedService {

    private final DeliveryMissedRepository deliveryMissedRepository;

    @Override
    public DeliveryMissed saveDelivery(DeliveryMissedRequest request) {
        return deliveryMissedRepository.saveDeliveryMissed(request);
    }
}
