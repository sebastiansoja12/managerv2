package com.warehouse.pickuppoint.application.service;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.application.exception.PickupPointNotFoundException;
import com.warehouse.pickuppoint.application.port.primary.PickupPointReadModelSyncPort;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointReadModelRepository;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointRepository;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import org.springframework.transaction.annotation.Transactional;

public class PickupPointReadModelSyncServiceImpl implements PickupPointReadModelSyncPort {

    private final PickupPointRepository pickupPointRepository;
    private final PickupPointReadModelRepository pickupPointReadModelRepository;

    public PickupPointReadModelSyncServiceImpl(
            final PickupPointRepository pickupPointRepository,
            final PickupPointReadModelRepository pickupPointReadModelRepository) {
        this.pickupPointRepository = pickupPointRepository;
        this.pickupPointReadModelRepository = pickupPointReadModelRepository;
    }

    @Override
    @Transactional
    public void syncReadModel(final PickupPointId pickupPointId) {
        final PickupPoint pickupPoint = this.pickupPointRepository.findById(pickupPointId)
                .orElseThrow(() -> new PickupPointNotFoundException(pickupPointId));
        this.pickupPointReadModelRepository.sync(pickupPoint.snapshot());
    }
}
