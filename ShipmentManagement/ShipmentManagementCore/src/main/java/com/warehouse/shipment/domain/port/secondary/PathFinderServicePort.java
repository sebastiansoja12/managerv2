package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.City;

public interface PathFinderServicePort {

    City determineDeliveryDepot(final Address address);
}
