package com.warehouse.deliverymissed.domain.service;

import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedDetailsEntity;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.id.DeliveryMissedDetailId;

import java.math.BigDecimal;
import java.time.Instant;

public class DeliveryMissedDetailsServiceImpl implements DeliveryMissedDetailsService {


    @Override
    public DeliveryMissedDetailsEntity recordDeliveryAttempt(final DeliveryMissedDetailId deliveryMissedDetailId,
                                                             final int attemptNumber,
                                                             final Instant deliveryDate,
                                                             final boolean addressChanged) {
        return null;
    }

    @Override
    public boolean shouldReturnToSender(final DeliveryMissedDetailId deliveryMissedDetailId) {
        return false;
    }

    @Override
    public BigDecimal calculatePenaltyFee(final DeliveryMissedDetailId deliveryMissedDetailId) {
        return null;
    }

    @Override
    public DeliveryMissedDetailsEntity updateDeliveryAddress(final DeliveryMissedDetailId deliveryMissedDetailId, final String newAddress) {
        return null;
    }

    @Override
    public DeliveryMissedDetailsEntity getDetails(final DeliveryMissedDetailId deliveryMissedDetailId) {
        return null;
    }

    @Override
    public DeliveryMissedDetailsEntity reportIncident(final DeliveryMissedDetailId deliveryMissedDetailId, final String incidentReport) {
        return null;
    }

    @Override
    public boolean isPackageDelivered(final DeliveryMissedDetailId deliveryMissedDetailId) {
        return false;
    }

    @Override
    public void removeDeliveryMissedDetails(final DeliveryMissedDetailId deliveryMissedDetailId) {

    }
}
