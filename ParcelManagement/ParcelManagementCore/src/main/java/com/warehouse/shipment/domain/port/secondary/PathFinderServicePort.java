package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;

public interface PathFinderServicePort {
    City determineDeliveryDepot(ShipmentParcel parcel);

    City determineDeliveryDepot(ParcelUpdate parcelUpdate);
}
