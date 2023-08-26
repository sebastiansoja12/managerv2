package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.model.Address;
import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;


public class PathFinderMockAdapter implements PathFinderServicePort {

    private final PathFinderMockService pathFinderMockService;

    public PathFinderMockAdapter(PathFinderMockService pathFinderMockService) {
        this.pathFinderMockService = pathFinderMockService;
    }

    @Override
    public City determineDeliveryDepot(ShipmentParcel parcel) {
        // deprecated
        return null;
    }

    @Override
    public City determineDeliveryDepot(Address address) {
        return pathFinderMockService.determineDeliveryDepot(address);
    }
}
