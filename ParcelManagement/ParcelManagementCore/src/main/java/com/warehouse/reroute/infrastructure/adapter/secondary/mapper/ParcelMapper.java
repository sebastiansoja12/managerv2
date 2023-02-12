package com.warehouse.reroute.infrastructure.adapter.secondary.mapper;

import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.vo.ParcelResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ParcelMapper {

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
    @Mapping(target = "parcelType", source = "parcelType")
    @Mapping(target = "parcelId.value", source = "id")
    ParcelResponse mapFromParcelEntityToParcelResponse(ParcelEntity parcelEntity);

    @Mapping(source = "parcel.sender.firstName", target = "firstName")
    @Mapping(source = "parcel.sender.lastName", target = "lastName")
    @Mapping(source = "parcel.sender.email", target = "senderEmail")
    @Mapping(source = "parcel.sender.telephoneNumber", target = "senderTelephone")
    @Mapping(source = "parcel.sender.city", target = "senderCity")
    @Mapping(source = "parcel.sender.postalCode", target = "senderPostalCode")
    @Mapping(source = "parcel.sender.street", target = "senderStreet")
    @Mapping(source = "parcel.recipient.firstName", target = "recipientFirstName")
    @Mapping(source = "parcel.recipient.lastName", target = "recipientLastName")
    @Mapping(source = "parcel.recipient.email", target = "recipientEmail")
    @Mapping(source = "parcel.recipient.telephoneNumber", target = "recipientTelephone")
    @Mapping(source = "parcel.recipient.city", target = "recipientCity")
    @Mapping(source = "parcel.recipient.postalCode", target = "recipientPostalCode")
    @Mapping(source = "parcel.recipient.street", target = "recipientStreet")
    @Mapping(source = "parcel.parcelType", target = "parcelType")
    ParcelEntity mapToParcelEntity(UpdateParcelRequest parcelRequest);

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
    @Mapping(source = "parcelType", target = "parcelType")
    ParcelEntity mapFromParcelResponseToParcelEntity(ParcelResponse parcelRequest);


}
