package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.vo.Price;

public interface PriceRepository {
    Price priceByShipmentSize(final ShipmentSize shipmentSize);
}
