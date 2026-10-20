package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.SourceShipment;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryDerivationType;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;

import java.util.Optional;

public class ShipmentServiceAdapter implements ShipmentServicePort {

    private final ShipmentApiService shipmentApiService;

    public ShipmentServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public Optional<SourceShipment> findSourceShipment(final ShipmentId shipmentId) {
        return this.shipmentApiService.findSourceShipment(shipmentId)
                .map(sourceShipment -> new SourceShipment(sourceShipment.shipmentId(),
                        derivationType(sourceShipment.shipmentStatus())));
    }

    @Override
    public Optional<ShipmentId> findDerivedShipment(final ShipmentId shipmentId) {
        return Optional.ofNullable(this.shipmentApiService.getShipment(shipmentId))
                .map(ShipmentDetailsDto::relatedShipmentId);
    }

    @Override
    public void markDelivered(final ShipmentId shipmentId) {
        this.shipmentApiService.markDelivered(shipmentId);
    }

    private DeliveryDerivationType derivationType(final ShipmentStatus sourceShipmentStatus) {
        return switch (sourceShipmentStatus) {
            case RETURN -> DeliveryDerivationType.RETURN;
            case REDIRECT -> DeliveryDerivationType.REDIRECT;
            default -> DeliveryDerivationType.SPLIT;
        };
    }
}
