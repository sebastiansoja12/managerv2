package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;

public interface PathFinderServicePort {

    VoronoiResponse determineDeliveryDepot(final Address address);
}
