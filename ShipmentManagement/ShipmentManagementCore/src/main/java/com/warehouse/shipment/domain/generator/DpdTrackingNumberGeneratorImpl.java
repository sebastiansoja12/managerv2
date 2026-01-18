package com.warehouse.shipment.domain.generator;

import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.shipment.domain.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.domain.vo.TrackingNumber;
import com.warehouse.shipment.domain.model.TrackingSequence;

@Component
public class DpdTrackingNumberGeneratorImpl implements TrackingNumberGenerator {

    private static final String SEQUENCE_ID = "DPD_SSCC";
    private static final String COMPANY_PREFIX = "5901234";
    private static final String EXTENSION = "3";

    private final TrackingSequenceRepository sequenceRepository;

    public DpdTrackingNumberGeneratorImpl(final TrackingSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public boolean canHandle(final CarrierOperator carrier) {
        return carrier == CarrierOperator.DPD;
    }

    @Override
    public TrackingNumber generate() {
        final TrackingSequence seq = sequenceRepository.findById(SEQUENCE_ID)
                .orElseGet(() ->
                        sequenceRepository.save(new TrackingSequence(SEQUENCE_ID, 1L))
                );

        final long serial = seq.next();
        final String base = EXTENSION + COMPANY_PREFIX + String.format("%09d", serial);
        final int checkDigit = calculateGs1CheckDigit(base);

        return new TrackingNumber(base + checkDigit);
    }

    private int calculateGs1CheckDigit(final String number) {
        int sum = 0;
        boolean multiplyByThree = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            sum += multiplyByThree ? digit * 3 : digit;
            multiplyByThree = !multiplyByThree;
        }
        return (10 - (sum % 10)) % 10;
    }
}

