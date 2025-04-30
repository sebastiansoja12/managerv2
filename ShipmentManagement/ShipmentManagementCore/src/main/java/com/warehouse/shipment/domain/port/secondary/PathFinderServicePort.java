package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Address;

public interface PathFinderServicePort {

    void determineDeliveryDepot(final Shipment shipment, final Address address);
}
