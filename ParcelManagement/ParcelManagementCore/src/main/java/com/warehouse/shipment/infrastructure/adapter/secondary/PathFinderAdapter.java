package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.model.Address;
import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.voronoi.VoronoiService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    @Override
    public City determineDeliveryDepot(Address address) {
        final String cityToDeliver = voronoiService.findFastestRoute(address.getCity());
        return new City(cityToDeliver);
    }
}
