package com.warehouse.logistics.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.logistics.application.port.secondary.ShipmentIdentificationServicePort;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentIdentificationServiceAdapter implements ShipmentIdentificationServicePort {

    private final ShipmentApiService shipmentApiService;

    public ShipmentIdentificationServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public Optional<TrackingNumber> findTrackingNumber(final ShipmentId shipmentId) {
        try {
            return Optional.ofNullable(shipmentApiService.getShipment(shipmentId))
                    .map(ShipmentDetailsDto::trackingNumber)
                    .map(TrackingNumber::new);
        } catch (final RuntimeException exception) {
            log.warn("Tracking number for shipment {} could not be identified: {}",
                    shipmentId.getValue(), exception.getMessage());
            return Optional.empty();
        }
    }
}
