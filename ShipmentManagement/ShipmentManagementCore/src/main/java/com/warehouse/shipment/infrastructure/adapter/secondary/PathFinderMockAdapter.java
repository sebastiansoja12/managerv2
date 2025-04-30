package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;


public class PathFinderMockAdapter implements PathFinderServicePort {

    private final PathFinderMockService pathFinderMockService;

    public PathFinderMockAdapter(PathFinderMockService pathFinderMockService) {
        this.pathFinderMockService = pathFinderMockService;
    }

    @Override
    public void determineDeliveryDepot(final Shipment shipment, final Address address) {
        final VoronoiResponse response = pathFinderMockService.determineDeliveryDepot(address);
        shipment.changeDestinationDepartment(response.getValue());
    }
}
