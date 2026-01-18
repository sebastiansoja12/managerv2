package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.TrackingSequence;

import java.util.Optional;

public interface TrackingSequenceRepository {
    Optional<TrackingSequence> findById(final String sequenceId);
    TrackingSequence save(final TrackingSequence trackingSequence);
}
