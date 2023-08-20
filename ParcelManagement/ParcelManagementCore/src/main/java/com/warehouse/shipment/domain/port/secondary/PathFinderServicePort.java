package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.Address;
import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.domain.model.ShipmentParcel;

public interface PathFinderServicePort {

    @Deprecated
    City determineDeliveryDepot(ShipmentParcel parcel);

    City determineDeliveryDepot(Address address);
}
