package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.service.TrackingNumberService;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberRule;

public class TrackingNumberGenerationService {

    private final TrackingNumberService trackingNumberService;
    private final TrackingNumberSequenceService trackingNumberSequenceService;

    public TrackingNumberGenerationService(final TrackingNumberService trackingNumberService,
                                           final TrackingNumberSequenceService trackingNumberSequenceService) {
        this.trackingNumberService = trackingNumberService;
        this.trackingNumberSequenceService = trackingNumberSequenceService;
    }

    public TrackingNumber generate(final TrackingNumberRule rule, final ShipmentId shipmentId) {
        return switch (rule.source()) {
            case SEQUENCE -> this.trackingNumberService.nextTrackingNumber(
                    rule, shipmentId, this.trackingNumberSequenceService.nextValue(rule));
            case SHIPMENT_ID, RANDOM -> this.trackingNumberService.nextTrackingNumber(rule, shipmentId);
        };
    }
}
