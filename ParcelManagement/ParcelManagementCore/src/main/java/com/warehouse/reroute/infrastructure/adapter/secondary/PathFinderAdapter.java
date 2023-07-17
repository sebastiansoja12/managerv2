package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.depot.api.DepotService;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.reroute.domain.model.City;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.port.secondary.PathFinderServicePort;
import com.warehouse.voronoi.VoronoiService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    private final DepotService depotService;

    @Override
    public City determineNewDeliveryDepot(Parcel parcel) {
        final List<DepotDto> depots = depotService.findAll();
        final String cityToDeliver = voronoiService.findFastestRoute(depots, parcel.getRecipient().getCity());
        return new City(cityToDeliver);
    }
}
