package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.Shipment;

public interface PriceService {
    void determineShipmentPrice(final Shipment shipment);
}
