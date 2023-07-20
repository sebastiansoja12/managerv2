package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.domain.model.ShipmentParcel;

public interface PathFinderServicePort {
    City determineNewDeliveryDepot(ShipmentParcel parcel);
}
