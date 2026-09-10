package com.warehouse.shipment.application.service;

import java.util.Locale;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.shipment.application.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberRule;

public class TrackingNumberSequenceService {

    private static final String DEFAULT_SEQUENCE_ID = "TRACKING_NUMBER";

    private final TrackingSequenceRepository sequenceRepository;

    public TrackingNumberSequenceService(final TrackingSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Transactional
    public long nextValue(final TrackingNumberRule rule) {
        final String sequenceId = DEFAULT_SEQUENCE_ID + "_" + rule.key().toUpperCase(Locale.ROOT);
        final TrackingSequence sequence = this.sequenceRepository.findById(sequenceId)
                .orElseGet(() -> new TrackingSequence(sequenceId, 1L));
        final long nextValue = sequence.next();
        this.sequenceRepository.save(sequence);
        return nextValue;
    }
}
