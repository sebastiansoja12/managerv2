package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.RerouteTokenNotFoundException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.service.ShipmentService;

import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@AllArgsConstructor
public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService service;

    private final Logger logger;

    @Override
    public ShipmentResponse ship(ShipmentRequest request) {
        final ShipmentParcel parcel = extractParcelFromRequest(request);

        logParcelShipment(parcel);

        if (ObjectUtils.isEmpty(parcel)) {
            throw new ParcelNotFoundException("Parcel not found in request");
        }

        prepareParcelToCreate(parcel);

        return service.createShipment(parcel);
    }

    @Override
    public void delete(Long parcelId) {
        logDeleteParcel(parcelId);
        service.delete(parcelId);
    }

    @Override
    public Parcel loadParcel(Long parcelId) {
        return service.loadParcel(parcelId);
    }

    @Override
    public UpdateParcelResponse update(UpdateParcelRequest updateParcelRequest) {
        validateParcelRequest(updateParcelRequest);
        final ParcelUpdate parcelUpdate = ParcelUpdate.builder().build();
        buildParcelProperties(ParcelUpdate.builder(), updateParcelRequest);
        buildParcelSender(ParcelUpdate.builder(), updateParcelRequest);
        buildParcelRecipient(ParcelUpdate.builder(), updateParcelRequest);
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

    private ShipmentParcel extractParcelFromRequest(ShipmentRequest request) {
        return request.getParcel();
    }

    private void prepareParcelToCreate(ShipmentParcel parcel) {
        parcel.setStatus(Status.CREATED);
        parcel.setParcelType(ParcelType.PARENT);
    }
    

	private void buildParcelRecipient(ParcelUpdate.ParcelUpdateBuilder parcelUpdate,
			UpdateParcelRequest updateParcelRequest) {
		parcelUpdate.recipientFirstName(updateParcelRequest.getParcel().getRecipient().getFirstName());
		parcelUpdate.recipientLastName(updateParcelRequest.getParcel().getRecipient().getLastName());
		parcelUpdate.recipientEmail(updateParcelRequest.getParcel().getRecipient().getEmail());
		parcelUpdate.recipientTelephone(updateParcelRequest.getParcel().getRecipient().getTelephoneNumber());
		parcelUpdate.recipientCity(updateParcelRequest.getParcel().getRecipient().getCity());
		parcelUpdate.recipientStreet(updateParcelRequest.getParcel().getRecipient().getStreet());
		parcelUpdate.recipientPostalCode(updateParcelRequest.getParcel().getRecipient().getPostalCode());
	}

	private void buildParcelSender(ParcelUpdate.ParcelUpdateBuilder parcelUpdate,
			UpdateParcelRequest updateParcelRequest) {
		parcelUpdate.senderFirstName(updateParcelRequest.getParcel().getSender().getFirstName());
		parcelUpdate.senderLastName(updateParcelRequest.getParcel().getSender().getLastName());
		parcelUpdate.senderEmail(updateParcelRequest.getParcel().getSender().getEmail());
		parcelUpdate.senderTelephone(updateParcelRequest.getParcel().getSender().getTelephoneNumber());
		parcelUpdate.senderCity(updateParcelRequest.getParcel().getSender().getCity());
		parcelUpdate.senderStreet(updateParcelRequest.getParcel().getSender().getStreet());
		parcelUpdate.senderPostalCode(updateParcelRequest.getParcel().getSender().getPostalCode());
	}

	private void buildParcelProperties(ParcelUpdate.ParcelUpdateBuilder parcelUpdate,
			UpdateParcelRequest updateParcelRequest) {
		parcelUpdate.id(updateParcelRequest.getParcel().getId());
		parcelUpdate.parcelType(updateParcelRequest.getParcel().getParcelType());
		parcelUpdate.parcelSize(updateParcelRequest.getParcel().getParcelSize());
	}


    private void logParcelShipment(ShipmentParcel parcel) {
        logger.info("Detected service to create shipment for parcel {0}", parcel.getClass());
    }

    private void logDeleteParcel(Long parcelId) {
        logger.info("Parcel to delete with id: {0}", parcelId);
    }

}
