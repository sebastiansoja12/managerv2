package com.warehouse.deliverymissed.domain.service;

import java.math.BigDecimal;
import java.time.Instant;

import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedDetailsEntity;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.id.DeliveryMissedDetailId;

public interface DeliveryMissedDetailsService {

    DeliveryMissedDetailsEntity recordDeliveryAttempt(
            DeliveryMissedDetailId deliveryMissedDetailId,
            int attemptNumber,
            Instant deliveryDate,
            boolean addressChanged
    );

    boolean shouldReturnToSender(DeliveryMissedDetailId deliveryMissedDetailId);

    BigDecimal calculatePenaltyFee(DeliveryMissedDetailId deliveryMissedDetailId);

    DeliveryMissedDetailsEntity updateDeliveryAddress(
            DeliveryMissedDetailId deliveryMissedDetailId,
            String newAddress
    );

    DeliveryMissedDetailsEntity getDetails(DeliveryMissedDetailId deliveryMissedDetailId);

    DeliveryMissedDetailsEntity reportIncident(
            DeliveryMissedDetailId deliveryMissedDetailId,
            String incidentReport
    );

    boolean isPackageDelivered(DeliveryMissedDetailId deliveryMissedDetailId);

    void removeDeliveryMissedDetails(DeliveryMissedDetailId deliveryMissedDetailId);
}

