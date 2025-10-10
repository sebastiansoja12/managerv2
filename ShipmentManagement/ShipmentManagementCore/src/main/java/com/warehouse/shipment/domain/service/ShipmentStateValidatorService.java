package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;

public interface ShipmentStateValidatorService {
    Result<Void, String> validateShipmentState(final Shipment shipment);
}
