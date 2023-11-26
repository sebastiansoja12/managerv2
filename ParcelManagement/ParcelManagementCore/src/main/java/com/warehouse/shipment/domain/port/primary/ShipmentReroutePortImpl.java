package com.warehouse.shipment.domain.port.primary;

import static com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes.SHIPMENT_204;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
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
        final ParcelUpdate.ParcelUpdateBuilder parcelUpdate = ParcelUpdate.builder();
        buildParcelProperties(parcelUpdate, updateParcelRequest);
        buildParcelSender(parcelUpdate, updateParcelRequest);
        buildParcelRecipient(parcelUpdate, updateParcelRequest);
        return service.update(parcelUpdate.build());
    }

    
    private void validateParcelRequest(UpdateParcelRequest updateParcelRequest) {
        if (updateParcelRequest.getParcel() == null) {
            throw new ParcelNotFoundException(SHIPMENT_204);
        } else if (updateParcelRequest.getParcel().getId() == null) {
            throw new ParcelNotFoundException(SHIPMENT_204);
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
		parcelUpdate.status(updateParcelRequest.getParcel().getParcelStatus());
	}

}
