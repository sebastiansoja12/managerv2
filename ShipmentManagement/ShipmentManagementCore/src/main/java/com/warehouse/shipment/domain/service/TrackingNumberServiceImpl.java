package com.warehouse.shipment.domain.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.shipment.domain.generator.TrackingNumberGenerator;
import com.warehouse.shipment.domain.vo.TrackingNumber;

@Service
public class TrackingNumberServiceImpl implements TrackingNumberService {

    private final Set<TrackingNumberGenerator> generators;

    public TrackingNumberServiceImpl(final Set<TrackingNumberGenerator> generators) {
        this.generators = generators;
    }

    @Override
    public TrackingNumber nextTrackingNumber(final CarrierOperator carrier) {
        return generators.stream()
                .filter(g -> g.canHandle(carrier))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No tracking number generator for carrier: " + carrier))
                .generate();
    }
}