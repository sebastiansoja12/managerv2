package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ParcelMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "sender.firstName", target = "firstName")
    @Mapping(source = "sender.lastName", target = "lastName")
    @Mapping(source = "sender.email", target = "senderEmail")
    @Mapping(source = "sender.telephoneNumber", target = "senderTelephone")
    @Mapping(source = "sender.city", target = "senderCity")
    @Mapping(source = "sender.postalCode", target = "senderPostalCode")
    @Mapping(source = "sender.street", target = "senderStreet")
    @Mapping(source = "recipient.firstName", target = "recipientFirstName")
    @Mapping(source = "recipient.lastName", target = "recipientLastName")
    @Mapping(source = "recipient.email", target = "recipientEmail")
    @Mapping(source = "recipient.telephoneNumber", target = "recipientTelephone")
    @Mapping(source = "recipient.city", target = "recipientCity")
    @Mapping(source = "recipient.postalCode", target = "recipientPostalCode")
    @Mapping(source = "recipient.street", target = "recipientStreet")
    @Mapping(source = "parcelSize", target = "parcelSize")
    @Mapping(source = "status", target = "status")
    ParcelEntity map(Parcel parcel);

    @Mapping(target = "id", source = "id")
    @Mapping(source = "sender.firstName", target = "firstName")
    @Mapping(source = "sender.lastName", target = "lastName")
    @Mapping(source = "sender.email", target = "senderEmail")
    @Mapping(source = "sender.telephoneNumber", target = "senderTelephone")
    @Mapping(source = "sender.city", target = "senderCity")
    @Mapping(source = "sender.postalCode", target = "senderPostalCode")
    @Mapping(source = "sender.street", target = "senderStreet")
    @Mapping(source = "recipient.firstName", target = "recipientFirstName")
    @Mapping(source = "recipient.lastName", target = "recipientLastName")
    @Mapping(source = "recipient.email", target = "recipientEmail")
    @Mapping(source = "recipient.telephoneNumber", target = "recipientTelephone")
    @Mapping(source = "recipient.city", target = "recipientCity")
    @Mapping(source = "recipient.postalCode", target = "recipientPostalCode")
    @Mapping(source = "recipient.street", target = "recipientStreet")
    @Mapping(source = "parcelSize", target = "parcelSize")
    @Mapping(source = "status", target = "status")
    ParcelEntity mapForUpdate(Parcel parcel);

    ParcelEntity map(ParcelUpdate parcel);

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
    Parcel map(ParcelEntity entity);


    @Mapping(target = "parcel.sender.firstName", source = "firstName")
    @Mapping(target = "parcel.sender.lastName", source = "lastName")
    @Mapping(target = "parcel.sender.email", source = "senderEmail")
    @Mapping(target = "parcel.sender.telephoneNumber", source = "senderTelephone")
    @Mapping(target = "parcel.sender.city", source = "senderCity")
    @Mapping(target = "parcel.sender.postalCode", source = "senderPostalCode")
    @Mapping(target = "parcel.sender.street", source = "senderStreet")
    @Mapping(target = "parcel.recipient.firstName", source = "recipientFirstName")
    @Mapping(target = "parcel.recipient.lastName", source = "recipientLastName")
    @Mapping(target = "parcel.recipient.email", source = "recipientEmail")
    @Mapping(target = "parcel.recipient.telephoneNumber", source = "recipientTelephone")
    @Mapping(target = "parcel.recipient.city", source = "recipientCity")
    @Mapping(target = "parcel.recipient.postalCode", source = "recipientPostalCode")
    @Mapping(target = "parcel.recipient.street", source = "recipientStreet")
    @Mapping(target = "parcel.parcelSize", source = "parcelSize")
    @Mapping(target = "parcel.id", source = "id")
    @Mapping(target = "parcel.parcelType", source = "parcelType")
    @Mapping(target = "parcel.status", source = "status")
    UpdateParcelResponse mapToUpdateParcelResponse(ParcelEntity entity);
}
