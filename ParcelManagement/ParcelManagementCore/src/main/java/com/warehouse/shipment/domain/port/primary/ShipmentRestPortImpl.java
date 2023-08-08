package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.service.ShipmentService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentRestPortImpl implements ShipmentRestPort {

    private final ShipmentService service;

    private final Logger logger;

    @Override
    public void delete(Long parcelId) {
        logDeleteParcel(parcelId);
        service.delete(parcelId);
    }

    @Override
    public Parcel loadParcel(Long parcelId) {
        return service.loadParcel(parcelId);
    }

    private void logDeleteParcel(Long parcelId) {
        logger.info("Parcel to delete with id: {}", parcelId);
    }

}
