package com.warehouse.reroute.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteParcel;
import com.warehouse.reroute.domain.model.RerouteParcelRequest;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;

@Mapper
public interface ParcelMapper {


    @Mapping(source = "id", target = "parcel.parcelId.value")
    @Mapping(source = "parcel.sender", target = "parcel.sender")
    @Mapping(source = "parcel.recipient", target = "parcel.recipient")
    UpdateParcelRequestDto map(RerouteParcelRequest updateParcelRequest);


    @Mapping(source = "parcelId.value", target = "parcel.parcelId.value")
    @Mapping(source = "rerouteParcel.sender", target = "parcel.sender")
    @Mapping(source = "rerouteParcel.recipient", target = "parcel.recipient")
    @Mapping(source = "rerouteParcel.parcelSize", target = "parcel.parcelSize")
    @Mapping(source = "rerouteParcel.status", target = "parcel.status")
    @Mapping(source = "rerouteParcel.parcelType", target = "parcel.parcelType")
    @Mapping(source = "rerouteParcel.parcelRelatedId", target = "parcel.parcelRelatedId")
    @Mapping(source = "rerouteParcel.destination", target = "parcel.destination")
    UpdateParcelRequestDto map(RerouteParcel rerouteParcel, ParcelId parcelId);
    
    @Mapping(source = "parcel.sender.firstName", target = "sender.firstName")
    @Mapping(source = "parcel.sender.lastName", target = "sender.lastName")
    @Mapping(source = "parcel.sender.email", target = "sender.email")
    @Mapping(source = "parcel.sender.telephoneNumber", target = "sender.telephoneNumber")
    @Mapping(source = "parcel.sender.city", target = "sender.city")
    @Mapping(source = "parcel.sender.postalCode", target = "sender.postalCode")
    @Mapping(source = "parcel.sender.street", target = "sender.street")
    @Mapping(source = "parcel.recipient.firstName", target = "recipient.firstName")
    @Mapping(source = "parcel.recipient.lastName", target = "recipient.lastName")
    @Mapping(source = "parcel.recipient.email", target = "recipient.email")
    @Mapping(source = "parcel.recipient.telephoneNumber", target = "recipient.telephoneNumber")
    @Mapping(source = "parcel.recipient.city", target = "recipient.city")
    @Mapping(source = "parcel.recipient.postalCode", target = "recipient.postalCode")
    @Mapping(source = "parcel.recipient.street", target = "recipient.street")
    @Mapping(source = "parcel.parcelSize", target = "parcelSize")
    @Mapping(source = "parcel.parcelId.value", target = "parcelId.value")
    @Mapping(source = "parcel.parcelType", target = "parcelType")
    @Mapping(source = "parcel.status", target = "status")
    @Mapping(source = "parcel.destination", target = "destination")
    @Mapping(source = "parcel.parcelRelatedId", target = "parcelRelatedId")
    Parcel map(UpdateParcelResponseDto updateParcelResponse);
}
