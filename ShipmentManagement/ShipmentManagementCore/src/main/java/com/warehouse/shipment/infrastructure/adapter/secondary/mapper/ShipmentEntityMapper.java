package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;

@Mapper
public interface ShipmentEntityMapper {

    default ParcelEntity map(final Shipment shipment) {
        return ParcelEntity.builder()
                .id(shipment.getShipmentId().getValue())
                .parcelRelatedId(shipment.getShipmentRelatedId() != null ? shipment.getShipmentRelatedId().getValue() : null)
                .shipmentType(shipment.getShipmentType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .destination(shipment.getDestination())
                .firstName(shipment.getSender().getFirstName())
                .lastName(shipment.getSender().getLastName())
                .senderPostalCode(shipment.getSender().getPostalCode())
                .senderTelephone(shipment.getSender().getTelephoneNumber())
                .senderCity(shipment.getSender().getCity())
                .senderStreet(shipment.getSender().getStreet())
                .senderEmail(shipment.getSender().getEmail())
                .recipientFirstName(shipment.getRecipient().getFirstName())
                .recipientLastName(shipment.getRecipient().getLastName())
                .recipientEmail(shipment.getRecipient().getEmail())
                .recipientTelephone(shipment.getRecipient().getTelephoneNumber())
                .recipientCity(shipment.getRecipient().getCity())
                .recipientPostalCode(shipment.getRecipient().getPostalCode())
                .recipientStreet(shipment.getRecipient().getStreet())
                .shipmentSize(shipment.getShipmentSize())
                .shipmentStatus(shipment.getShipmentStatus())
                .locked(shipment.isLocked())
                .build();
    }
}
