package com.warehouse.qrcode.infrastructure.adapter.secondary.mapper;


import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.infrastructure.adapter.secondary.entity.ShipmentEntity;

public abstract class ShipmentMapper {


    public static Shipment map(final ShipmentEntity shipmentEntity) {
        final Shipment shipment = new Shipment();
        shipment.setId(shipmentEntity.getShipmentId().getValue());
        shipment.setFirstName(shipmentEntity.getFirstName());
        shipment.setLastName(shipmentEntity.getLastName());
        shipment.setDestination(shipmentEntity.getDestination());
        shipment.setSenderEmail(shipmentEntity.getSenderEmail());
        shipment.setSenderStreet(shipmentEntity.getSenderStreet());
        shipment.setSenderCity(shipmentEntity.getSenderCity());
        shipment.setSenderTelephone(shipmentEntity.getSenderTelephone());
        shipment.setSenderEmail(shipmentEntity.getSenderEmail());
        shipment.setRecipientStreet(shipmentEntity.getRecipientStreet());
        shipment.setRecipientCity(shipmentEntity.getRecipientCity());
        shipment.setRecipientTelephone(shipmentEntity.getRecipientTelephone());
        shipment.setRecipientFirstName(shipmentEntity.getRecipientFirstName());
        shipment.setRecipientLastName(shipmentEntity.getRecipientLastName());
        shipment.setRecipientEmail(shipmentEntity.getRecipientEmail());

        return shipment;

    }
}
