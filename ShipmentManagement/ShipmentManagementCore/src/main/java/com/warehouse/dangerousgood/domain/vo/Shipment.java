package com.warehouse.dangerousgood.domain.vo;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.entity.ShipmentEntity;

public record Shipment(ShipmentId shipmentId, Country issuerCountry, Country receiverCountry) {
    public static Shipment from(final ShipmentEntity shipmentEntity) {
        return new Shipment(shipmentEntity.getShipmentId(), shipmentEntity.getIssuerCountry(), shipmentEntity.getReceiverCountry());
    }
}
