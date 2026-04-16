package com.warehouse.deliverymissed.domain.service;

import java.util.UUID;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedDetailsRepository;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.port.secondary.ShipmentUpdateServicePort;
import com.warehouse.deliverymissed.domain.vo.DeliveryAttemptNumber;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.id.DeliveryMissedDetailId;

public class DeliveryMissedServiceImpl implements DeliveryMissedService {

    private final DeliveryMissedRepository deliveryMissedRepository;

    private final ShipmentUpdateServicePort shipmentUpdateServicePort;

    private final DeliveryMissedDetailsRepository deliveryMissedDetailsRepository;

    public DeliveryMissedServiceImpl(final DeliveryMissedRepository deliveryMissedRepository,
                                     final ShipmentUpdateServicePort shipmentUpdateServicePort,
                                     final DeliveryMissedDetailsRepository deliveryMissedDetailsRepository) {
        this.deliveryMissedRepository = deliveryMissedRepository;
        this.shipmentUpdateServicePort = shipmentUpdateServicePort;
        this.deliveryMissedDetailsRepository = deliveryMissedDetailsRepository;
    }

    @Override
    public DeliveryMissed saveDelivery(final DeliveryMissedRequest request) {
        return deliveryMissedRepository.saveDeliveryMissed(request);
    }

    @Override
    public DeliveryAttemptNumber countDeliveryAttempts(final ShipmentId shipmentId) {
        final Integer attemptNumber = this.deliveryMissedDetailsRepository.count(shipmentId);
        return new DeliveryAttemptNumber(attemptNumber);
    }

    @Override
    public DeliveryMissedDetailId nextId() {
        return new DeliveryMissedDetailId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
    }
}
