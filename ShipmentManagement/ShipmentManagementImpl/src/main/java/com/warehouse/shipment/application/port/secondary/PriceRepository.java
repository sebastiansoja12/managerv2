package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.vo.Price;

public interface PriceRepository {
    Price priceByShipmentSize(final ShipmentSize shipmentSize, final Currency currency);
}
