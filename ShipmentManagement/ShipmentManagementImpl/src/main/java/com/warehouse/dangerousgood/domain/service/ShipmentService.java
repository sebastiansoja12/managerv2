package com.warehouse.dangerousgood.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.dangerousgood.domain.vo.Shipment;

public interface ShipmentService {
    Shipment findById(final ShipmentId shipmentId);
}
