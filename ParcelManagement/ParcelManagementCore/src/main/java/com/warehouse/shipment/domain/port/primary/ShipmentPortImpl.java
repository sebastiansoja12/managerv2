package com.warehouse.shipment.domain.port.primary;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentResponse;

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

        parcel.prepareParcelToCreate();

        return service.createShipment(parcel);
    }

    @Override
    public Parcel loadParcel(Long parcelId) {
        return service.loadParcel(parcelId);
    }

    @Override
    public boolean exists(Long parcelId) {
        return service.exists(parcelId);
    }


    private ShipmentParcel extractParcelFromRequest(ShipmentRequest request) {
        return request.getParcel();
    }

    private void logParcelShipment(ShipmentParcel parcel) {
		logger.info("Detected service to create shipment for parcel with telephone number {}",
				parcel.getSender().getTelephoneNumber());
    }

}
