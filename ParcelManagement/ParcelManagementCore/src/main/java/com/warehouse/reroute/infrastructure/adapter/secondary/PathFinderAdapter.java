package com.warehouse.reroute.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.depot.domain.vo.Coordinates;
import com.warehouse.dto.CoordinatesDto;
import com.warehouse.dto.DepotDto;
import com.warehouse.reroute.domain.model.City;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.port.secondary.PathFinderServicePort;
import com.warehouse.voronoi.VoronoiService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    private final DepotPort depotPort;

    @Override
    public City determineNewDeliveryDepot(Parcel parcel) {
		final List<Depot> depots = depotPort.findAll();
		final List<DepotDto> depotsRequest = depots.stream().map(this::prepareDepotRequest).toList();
        final String cityToDeliver = voronoiService.findFastestRoute(depotsRequest, parcel.getRecipient().getCity());
        return new City(cityToDeliver);
    }
    
    private DepotDto prepareDepotRequest(Depot depot) {
		return new DepotDto(depot.getCity(), depot.getStreet(), depot.getCountry(), depot.getDepotCode(),
				mapToCoordinatesDto(depot.getCoordinates()));
    }
    
    private CoordinatesDto mapToCoordinatesDto(Coordinates coordinates) {
        return new CoordinatesDto(coordinates.getLat(), coordinates.getLon());
    }
    
}
