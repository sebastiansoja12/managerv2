package com.warehouse.shipment.domain.generator;

import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.domain.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.domain.vo.TrackingNumber;

@Component
public class UpsTrackingNumberGeneratorImpl implements TrackingNumberGenerator {

    private static final String SEQUENCE_ID = "UPS";
    private static final String PREFIX = "1Z";

    private final TrackingSequenceRepository sequenceRepository;

    public UpsTrackingNumberGeneratorImpl(final TrackingSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public boolean canHandle(CarrierOperator carrier) {
        return carrier == CarrierOperator.UPS;
    }

    @Override
    public TrackingNumber generate() {
        final TrackingSequence seq = sequenceRepository.findById(SEQUENCE_ID)
                .orElseGet(() ->
                        sequenceRepository.save(new TrackingSequence(SEQUENCE_ID, 1L))
                );

        final long serial = seq.next();

        final String part1 = String.format("%06d", serial % 1_000_000);
        final String part2 = "AB";
        final String part3 = String.format("%08d", serial);

        return new TrackingNumber(PREFIX + part1 + part2 + part3);
    }
}

