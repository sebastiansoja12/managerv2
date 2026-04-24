package com.warehouse.shipment.domain.generator;

import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.domain.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.domain.vo.TrackingNumber;

@Component
public class DhlTrackingNumberGeneratorImpl implements TrackingNumberGenerator {

    private static final String SEQUENCE_ID = "DHL";
    private static final String PREFIX = "JD";

    private final TrackingSequenceRepository sequenceRepository;

    public DhlTrackingNumberGeneratorImpl(final TrackingSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public boolean canHandle(final CarrierOperator carrier) {
        return carrier == CarrierOperator.DHL;
    }

    @Override
    public TrackingNumber generate() {
        final TrackingSequence seq = sequenceRepository.findById(SEQUENCE_ID)
                .orElseGet(() ->
                        sequenceRepository.save(new TrackingSequence(SEQUENCE_ID, 1L))
                );

        final long serial = seq.next();
        return new TrackingNumber(PREFIX + String.format("%018d", serial));
    }
}

