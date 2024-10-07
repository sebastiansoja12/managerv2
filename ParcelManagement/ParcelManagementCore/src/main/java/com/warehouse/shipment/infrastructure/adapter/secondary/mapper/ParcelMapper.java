package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ParcelMapper {

    default ParcelEntity map(final ShipmentParcel parcel) {
        return ParcelEntity.builder()
                .parcelRelatedId(parcel.getParcelRelatedId() != null ? parcel.getParcelRelatedId().getId() : null)
                .parcelType(parcel.getParcelType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .destination(parcel.getDestination())
                .firstName(parcel.getSender().firstName())
                .lastName(parcel.getSender().lastName())
                .senderPostalCode(parcel.getSender().postalCode())
                .senderTelephone(parcel.getSender().telephoneNumber())
                .senderCity(parcel.getSender().city())
                .senderStreet(parcel.getSender().street())
                .senderEmail(parcel.getSender().email())
                .recipientFirstName(parcel.getRecipient().firstName())
                .recipientLastName(parcel.getRecipient().lastName())
                .recipientEmail(parcel.getRecipient().email())
                .recipientTelephone(parcel.getRecipient().telephoneNumber())
                .recipientCity(parcel.getRecipient().city())
                .recipientPostalCode(parcel.getRecipient().postalCode())
                .recipientStreet(parcel.getRecipient().street())
                .shipmentSize(parcel.getShipmentSize())
                .build();
    }

    @Mapping(source = "senderFirstName", target = "firstName")
    @Mapping(source = "senderLastName", target = "lastName")
    ParcelEntity map(ShipmentUpdate parcel);

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
    @Mapping(target = "parcelSize", source = "parcelSize")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "parcelStatus", source = "status")
    Parcel map(ParcelEntity entity);
}
