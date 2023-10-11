package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.dto.DepotDto;
import com.warehouse.reroute.domain.model.City;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.port.secondary.PathFinderServicePort;
import com.warehouse.voronoi.VoronoiService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    @Override
    public City determineNewDeliveryDepot(Parcel parcel) {
        final String cityToDeliver = voronoiService.findFastestRoute(parcel.getRecipient().getCity());
        return new City(cityToDeliver);
    }
    
    private DepotDto prepareDepotRequest(Depot depot) {
		return new DepotDto(depot.getCity(), depot.getStreet(), depot.getCountry(), depot.getDepotCode());
    }
}
