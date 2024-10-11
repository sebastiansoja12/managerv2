package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ParcelMapper {

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
                .locked(false)
                .build();
    }


    default ParcelEntity map(final ShipmentUpdate shipment) {
        return ParcelEntity.builder()
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
                .locked(false)
                .build();
    }

    @Mapping(target = "sender.firstName", source = "firstName")
    @Mapping(target = "sender.lastName", source = "lastName")
    @Mapping(target = "sender.email", source = "senderEmail")
    @Mapping(target = "sender.telephoneNumber", source = "senderTelephone")
    @Mapping(target = "sender.city", source = "senderCity")
    @Mapping(target = "sender.postalCode", source = "senderPostalCode")
    @Mapping(target = "sender.street", source = "senderStreet")
    @Mapping(target = "recipient.firstName", source = "recipientFirstName")
    @Mapping(target = "recipient.lastName", source = "recipientLastName")
    @Mapping(target = "recipient.email", source = "recipientEmail")
    @Mapping(target = "recipient.telephoneNumber", source = "recipientTelephone")
    @Mapping(target = "recipient.city", source = "recipientCity")
    @Mapping(target = "recipient.postalCode", source = "recipientPostalCode")
    @Mapping(target = "recipient.street", source = "recipientStreet")
    @Mapping(target = "shipmentRelatedId.value", source = "parcelRelatedId")
    Parcel map(final ParcelEntity entity);

    @Mapping(target = "sender.firstName", source = "firstName")
    @Mapping(target = "sender.lastName", source = "lastName")
    @Mapping(target = "sender.email", source = "senderEmail")
    @Mapping(target = "sender.telephoneNumber", source = "senderTelephone")
    @Mapping(target = "sender.city", source = "senderCity")
    @Mapping(target = "sender.postalCode", source = "senderPostalCode")
    @Mapping(target = "sender.street", source = "senderStreet")
    @Mapping(target = "recipient.firstName", source = "recipientFirstName")
    @Mapping(target = "recipient.lastName", source = "recipientLastName")
    @Mapping(target = "recipient.email", source = "recipientEmail")
    @Mapping(target = "recipient.telephoneNumber", source = "recipientTelephone")
    @Mapping(target = "recipient.city", source = "recipientCity")
    @Mapping(target = "recipient.postalCode", source = "recipientPostalCode")
    @Mapping(target = "recipient.street", source = "recipientStreet")
    @Mapping(target = "shipmentId.value", source = "id")
    @Mapping(target = "shipmentRelatedId.value", source = "parcelRelatedId")
    Shipment mapToShipment(final ParcelEntity entity);
}
