package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.domain.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.TrackingSequenceEntity;

import java.util.Optional;

public class TrackingSequenceRepositoryImpl implements TrackingSequenceRepository {
    
    private final TrackingSequenceReadRepository repository;
    
    public TrackingSequenceRepositoryImpl(final TrackingSequenceReadRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<TrackingSequence> findById(final String sequenceId) {
		return repository.findById(sequenceId)
				.map(sequence -> new TrackingSequence(sequence.getId(), sequence.getNextValue(), sequence.getVersion()));
    }

    @Override
    public TrackingSequence save(final TrackingSequence trackingSequence) {
        final TrackingSequenceEntity entity = new TrackingSequenceEntity(
                trackingSequence.getId(),
                trackingSequence.getNextValue(),
                trackingSequence.getVersion()
        );

        final TrackingSequenceEntity savedEntity = repository.saveAndFlush(entity);

        return new TrackingSequence(savedEntity.getId(), savedEntity.getNextValue(), savedEntity.getVersion());
    }

}
