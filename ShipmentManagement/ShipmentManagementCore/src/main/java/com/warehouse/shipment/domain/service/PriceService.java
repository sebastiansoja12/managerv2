package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.vo.Price;

public interface PriceService {
    Price determineShipmentPrice(final ShipmentSize shipmentSize);
}
