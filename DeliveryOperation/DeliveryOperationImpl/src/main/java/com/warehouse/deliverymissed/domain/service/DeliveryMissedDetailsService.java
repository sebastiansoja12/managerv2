package com.warehouse.deliverymissed.domain.service;

import java.math.BigDecimal;
import java.time.Instant;

import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedDetailsEntity;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.id.DeliveryMissedDetailId;

public interface DeliveryMissedDetailsService {

    DeliveryMissedDetailsEntity recordDeliveryAttempt(
            final DeliveryMissedDetailId deliveryMissedDetailId,
            final int attemptNumber,
            final Instant deliveryDate,
            final boolean addressChanged
    );

    boolean shouldReturnToSender(final DeliveryMissedDetailId deliveryMissedDetailId);

    BigDecimal calculatePenaltyFee(final DeliveryMissedDetailId deliveryMissedDetailId);

    DeliveryMissedDetailsEntity updateDeliveryAddress(
            final DeliveryMissedDetailId deliveryMissedDetailId,
            final String newAddress
    );

    DeliveryMissedDetailsEntity getDetails(final DeliveryMissedDetailId deliveryMissedDetailId);

    DeliveryMissedDetailsEntity reportIncident(
            final DeliveryMissedDetailId deliveryMissedDetailId,
            final String incidentReport
    );

    boolean isPackageDelivered(final DeliveryMissedDetailId deliveryMissedDetailId);

    void removeDeliveryMissedDetails(final DeliveryMissedDetailId deliveryMissedDetailId);

    DeliveryMissedDetailId nextDeliveryMissedDetailId();
}

