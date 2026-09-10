package com.warehouse.shipment.application.port.secondary;

import com.warehouse.shipment.domain.model.TrackingSequence;

import java.util.Optional;

public interface TrackingSequenceRepository {
    Optional<TrackingSequence> findById(final String sequenceId);
    TrackingSequence save(final TrackingSequence trackingSequence);
}
