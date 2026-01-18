package com.warehouse.shipment.domain.generator;

import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.shipment.domain.vo.TrackingNumber;

@Component
public class DefaultTrackingNumberGeneratorImpl implements TrackingNumberGenerator {

    @Override
    public boolean canHandle(final CarrierOperator carrier) {
        return carrier.equals(CarrierOperator.DEFAULT);
    }

    @Override
    public TrackingNumber generate() {
        final long timestamp = System.currentTimeMillis() % 1_000_000_000;
        final int random = (int)(Math.random() * 9000) + 1000;
        return new TrackingNumber("XX" + timestamp + random);
    }
}

