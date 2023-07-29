package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.RerouteTokenNotFoundException;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.UpdateParcelRequest;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;
import com.warehouse.shipment.domain.service.ShipmentService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentReroutePortImpl implements ShipmentReroutePort {
    
    private final ShipmentService service;
    
    @Override
    public UpdateParcelResponse reroute(UpdateParcelRequest updateParcelRequest) {
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

}
