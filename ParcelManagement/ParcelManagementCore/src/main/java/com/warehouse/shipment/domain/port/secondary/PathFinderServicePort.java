package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.Address;
import com.warehouse.shipment.domain.model.City;

public interface PathFinderServicePort {

    City determineDeliveryDepot(Address address);
}
