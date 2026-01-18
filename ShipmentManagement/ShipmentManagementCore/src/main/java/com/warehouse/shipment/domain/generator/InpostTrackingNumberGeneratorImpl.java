package com.warehouse.shipment.domain.generator;

import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.domain.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.domain.vo.TrackingNumber;

@Component
public class InpostTrackingNumberGeneratorImpl implements TrackingNumberGenerator {

    private static final String SEQUENCE_ID = "INPOST";
    private static final String PREFIX = "RR";

    private final TrackingSequenceRepository sequenceRepository;

    public InpostTrackingNumberGeneratorImpl(final TrackingSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public boolean canHandle(final CarrierOperator carrier) {
        return carrier == CarrierOperator.INPOST;
    }

    @Override
    public TrackingNumber generate() {
        final TrackingSequence seq = sequenceRepository.findById(SEQUENCE_ID)
                .orElseGet(() ->
                        sequenceRepository.save(new TrackingSequence(SEQUENCE_ID, 1L))
                );

        final long serial = seq.next();
        final String base = PREFIX + String.format("%09d", serial);

        final int checkDigit = calculateMod10(base);

        return new TrackingNumber(base + checkDigit);
    }

    private int calculateMod10(final String number) {
        int sum = 0;
        boolean multiplyBy3 = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            sum += multiplyBy3 ? digit * 3 : digit;
            multiplyBy3 = !multiplyBy3;
        }
        return (10 - (sum % 10)) % 10;
    }
}

