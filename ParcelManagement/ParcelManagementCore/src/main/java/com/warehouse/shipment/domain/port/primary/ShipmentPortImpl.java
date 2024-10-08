package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.*;
import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.service.ShipmentService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService service;

    private final Logger logger;

    @Override
    public ShipmentResponse ship(final ShipmentRequest request) {
        final Shipment shipment = extractShipmentFromRequest(request);

        if (ObjectUtils.isEmpty(shipment)) {
            throw new ParcelNotFoundException(ShipmentExceptionCodes.SHIPMENT_204);
        }

        logShipment(shipment);

        shipment.prepareShipmentToCreate();

        return service.createShipment(shipment);
    }

    @Override
    public ShipmentUpdateResponse update(final ShipmentUpdateRequest request) {
        final ShipmentUpdate shipmentUpdate = ShipmentUpdate.from(request);

        return service.update(shipmentUpdate);
    }

    @Override
    public Parcel loadParcel(final ShipmentId shipmentId) {
        return service.loadParcel(shipmentId);
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        return service.exists(shipmentId);
    }


    private Shipment extractShipmentFromRequest(final ShipmentRequest request) {
        return request.getShipment();
    }

    private void logShipment(Shipment parcel) {
		logger.info("Detected service to create shipment with telephone number {}",
				parcel.getSender().telephoneNumber());
    }

}
