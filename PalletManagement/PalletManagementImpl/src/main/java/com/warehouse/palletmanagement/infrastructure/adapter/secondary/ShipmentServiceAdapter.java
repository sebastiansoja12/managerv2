package com.warehouse.palletmanagement.infrastructure.adapter.secondary;

import com.warehouse.palletmanagement.domain.vo.PalletDocumentShipment;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.domain.vo.PalletShipment;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ShipmentServiceAdapter implements ShipmentServicePort {

    private static final Logger log = LoggerFactory.getLogger(ShipmentServiceAdapter.class);

    private final ShipmentApiService shipmentApiService;

    public ShipmentServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public ShipmentLoadResult loadOnPallet(final ShipmentId shipmentId) {
        try {
            final ShipmentDetailsDto shipment = this.shipmentApiService.loadShipmentOnPallet(shipmentId);
            return ShipmentLoadResult.loaded(shipmentId, shipment.status().name());
        } catch (final RuntimeException exception) {
            log.warn("Shipment {} could not be loaded on pallet: {}", shipmentId.getValue(), exception.getMessage());
            return ShipmentLoadResult.failed(shipmentId, exception.getMessage());
        }
    }

    @Override
    public Optional<PalletDocumentShipment> describeShipment(final ShipmentId shipmentId) {
        try {
            return Optional.ofNullable(this.shipmentApiService.getShipment(shipmentId))
                    .map(shipment -> new PalletDocumentShipment(shipmentId, shipment.trackingNumber(), shipment.status(),
                            shipment.recipientCity(), shipment.recipientPostalCode(), shipment.weightInKilograms()));
        } catch (final RuntimeException exception) {
            log.warn("Shipment {} could not be described: {}", shipmentId.getValue(), exception.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<PalletShipment> findShipment(final ShipmentId shipmentId) {
        try {
            return Optional.ofNullable(this.shipmentApiService.getShipment(shipmentId))
                    .map(shipment -> new PalletShipment(shipmentId, shipment.status(), shipment.weightInKilograms()));
        } catch (final RuntimeException exception) {
            log.warn("Shipment {} could not be read: {}", shipmentId.getValue(), exception.getMessage());
            return Optional.empty();
        }
    }
}
