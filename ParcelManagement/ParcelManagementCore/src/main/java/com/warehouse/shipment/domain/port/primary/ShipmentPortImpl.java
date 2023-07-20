package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.RerouteTokenNotFoundException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.service.ShipmentService;

import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@AllArgsConstructor
public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService service;

    @Override
    public ShipmentResponse ship(ShipmentRequest request) {
        final ShipmentParcel parcel = extractParcelFromRequest(request);
        if (ObjectUtils.isEmpty(parcel)) {
            throw new ParcelNotFoundException("Parcel not found in request");
        }
        parcel.setStatus(Status.CREATED);
        parcel.setParcelType(ParcelType.PARENT);
        return service.createShipment(parcel);
    }

    private ShipmentParcel extractParcelFromRequest(ShipmentRequest request) {
        return request.getParcel();
    }

    @Override
    public void delete(Long parcelId) {
        service.delete(parcelId);
    }

    @Override
    public Parcel loadParcel(Long parcelId) {
        return service.loadParcel(parcelId);
    }

    @Override
    public UpdateParcelResponse update(UpdateParcelRequest updateParcelRequest) {
        validateParcelRequest(updateParcelRequest);
        final ParcelUpdate parcelUpdate = ParcelUpdate.builder()
                .id(updateParcelRequest.getParcel().getId())
                .parcelSize(updateParcelRequest.getParcel().getParcelSize())
                .senderFirstName(updateParcelRequest.getParcel().getSender().getFirstName())
                .senderLastName(updateParcelRequest.getParcel().getSender().getLastName())
                .senderEmail(updateParcelRequest.getParcel().getSender().getEmail())
                .senderTelephone(updateParcelRequest.getParcel().getSender().getTelephoneNumber())
                .senderCity(updateParcelRequest.getParcel().getSender().getCity())
                .senderStreet(updateParcelRequest.getParcel().getSender().getStreet())
                .senderPostalCode(updateParcelRequest.getParcel().getSender().getPostalCode())
                .recipientFirstName(updateParcelRequest.getParcel().getRecipient().getFirstName())
                .recipientLastName(updateParcelRequest.getParcel().getRecipient().getLastName())
                .recipientEmail(updateParcelRequest.getParcel().getRecipient().getEmail())
                .recipientTelephone(updateParcelRequest.getParcel().getRecipient().getTelephoneNumber())
                .recipientCity(updateParcelRequest.getParcel().getRecipient().getCity())
                .recipientStreet(updateParcelRequest.getParcel().getRecipient().getStreet())
                .recipientPostalCode(updateParcelRequest.getParcel().getRecipient().getPostalCode())
                .parcelType(updateParcelRequest.getParcel().getParcelType())
                .status(updateParcelRequest.getParcel().getStatus())
                .build();
        return service.update(parcelUpdate);
    }

    private void validateParcelRequest(UpdateParcelRequest updateParcelRequest) {
        if (updateParcelRequest.getParcel() == null) {
            throw new ParcelNotFoundException("Parcel ID is null");
        } else if (updateParcelRequest.getToken() == null) {
            throw new RerouteTokenNotFoundException("Reroute token is null");
        } else if (updateParcelRequest.getParcel().getId() == null) {
            throw new ParcelNotFoundException("Parcel is null");
        }
    }
}
