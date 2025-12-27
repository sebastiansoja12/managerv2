package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.DangerousGoodEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;

@Mapper
public interface ShipmentEntityMapper {

    default ShipmentEntity map(final Shipment shipment) {
        return ShipmentEntity.builder()
                .shipmentId(shipment.getShipmentId())
                .shipmentRelatedId(shipment.getShipmentRelatedId())
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
                .price(shipment.getPrice())
                .shipmentPriority(shipment.getShipmentPriority())
                .signature(SignatureEntity.from(shipment.getSignature()))
                .dangerousGood(DangerousGoodEntity.from(shipment.getDangerousGood()))
                .externalRouteId(shipment.getExternalRouteId())
                .externalReturnId(shipment.getExternalReturnId())
                .build();
    }
}
