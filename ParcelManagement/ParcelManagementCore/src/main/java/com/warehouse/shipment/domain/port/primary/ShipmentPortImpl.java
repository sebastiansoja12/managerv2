package com.warehouse.shipment.domain.port.primary;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.model.ShipmentRequest;
import com.warehouse.shipment.domain.model.ShipmentResponse;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService service;

    private final Logger logger;

    @Override
    public ShipmentResponse ship(ShipmentRequest request) {
        final ShipmentParcel parcel = extractParcelFromRequest(request);

        if (ObjectUtils.isEmpty(parcel)) {
            throw new ParcelNotFoundException(ShipmentExceptionCodes.SHIPMENT_204);
        }

        logParcelShipment(parcel);

        prepareParcelToCreate(parcel);

        return service.createShipment(parcel);
    }

    private ShipmentParcel extractParcelFromRequest(ShipmentRequest request) {
        return request.getParcel();
    }

    private void prepareParcelToCreate(ShipmentParcel parcel) {
        parcel.setStatus(Status.CREATED);
        parcel.setParcelType(ParcelType.PARENT);
    }

    private void logParcelShipment(ShipmentParcel parcel) {
		logger.info("Detected service to create shipment for parcel with telephone number {}",
				parcel.getSender().getTelephoneNumber());
    }

    private void logDeleteParcel(Long parcelId) {
        logger.info("Parcel to delete with id: {}", parcelId);
    }

}
