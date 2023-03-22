package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.ShipmentPort;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentPort shipmentPort;

    private final ShipmentRepository shipmentRepository;

    @Override
    public ShipmentResponse ship(ShipmentRequest request) {
        return shipmentPort.ship(request);
    }

    @Override
    public Parcel loadParcel(Long parcelId) {
        return shipmentRepository.loadParcelById(parcelId);
    }

    @Override
    public UpdateParcelResponse update(ParcelUpdate parcelUpdate) {
        return shipmentPort.update(parcelUpdate);
    }

    @Override
    public void delete(Long parcelId) {
        shipmentRepository.delete(parcelId);
    }
}
