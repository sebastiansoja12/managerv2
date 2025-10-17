package com.warehouse.qrcode.infrastructure.adapter.secondary;

import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.port.secondary.ShipmentRepository;
import com.warehouse.qrcode.domain.vo.ShipmentId;
import com.warehouse.qrcode.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;
import com.warehouse.qrcode.infrastructure.adapter.secondary.mapper.ShipmentMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final ShipmentReadRepository shipmentReadRepository;


    private final String exceptionMessage = "Shipment %s was not found";

    @Override
    public Shipment find(final ShipmentId shipmentId) {
        return shipmentReadRepository.findById(shipmentId).map(ShipmentMapper::map).orElseThrow(
                () -> new ShipmentNotFoundException(String.format(exceptionMessage, shipmentId.getValue()))
        );
    }
}
