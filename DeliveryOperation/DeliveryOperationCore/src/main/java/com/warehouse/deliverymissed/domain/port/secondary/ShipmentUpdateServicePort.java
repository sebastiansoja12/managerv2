package com.warehouse.deliverymissed.domain.port.secondary;


import com.warehouse.deliverymissed.domain.vo.UpdateStatusShipmentRequest;

public interface ShipmentUpdateServicePort {
    void updateShipmentStatus(final UpdateStatusShipmentRequest updateStatusShipmentRequest);
}
