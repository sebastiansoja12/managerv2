package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.domain.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.TrackingSequenceEntity;

public class TrackingSequenceRepositoryImpl implements TrackingSequenceRepository {
    
    private final TrackingSequenceReadRepository repository;
    
    public TrackingSequenceRepositoryImpl(final TrackingSequenceReadRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<TrackingSequence> findById(final String sequenceId) {
		return repository.findById(sequenceId)
				.map(sequence -> new TrackingSequence(sequence.getId(), sequence.getNextValue()));
    }

    @Override
    public TrackingSequence save(final TrackingSequence trackingSequence) {
        final TrackingSequenceEntity entity = new TrackingSequenceEntity(
                trackingSequence.getId(),
                trackingSequence.getNextValue()
        );

        final TrackingSequenceEntity savedEntity = repository.save(entity);

        return new TrackingSequence(savedEntity.getId(), savedEntity.getNextValue());
    }

}
