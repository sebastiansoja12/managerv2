package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentIdentificationServicePort;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentTrackingDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentIdentificationServiceAdapter implements ShipmentIdentificationServicePort {

    private final ShipmentApiService shipmentApiService;

    public ShipmentIdentificationServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public Optional<ShipmentId> findShipmentId(final TrackingNumber trackingNumber) {
        if (trackingNumber == null || trackingNumber.value() == null || trackingNumber.value().isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(shipmentApiService.getShipmentTracking(new TrackingNumber(trackingNumber.value().trim())))
                    .map(ShipmentTrackingDto::shipmentId);
        } catch (final RuntimeException exception) {
            log.warn("Shipment with tracking number {} could not be identified: {}",
                    trackingNumber.value(), exception.getMessage());
            return Optional.empty();
        }
    }
}
