package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.depot.api.dto.CoordinatesDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.depot.domain.vo.Coordinates;
import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.voronoi.VoronoiService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    private final DepotPort depotPort;

    @Override
    public City determineDeliveryDepot(ShipmentParcel parcel) {
        final List<Depot> depots = depotPort.findAll();
        final List<DepotDto> depotsRequest = depots.stream().map(this::prepareDepotRequest).toList();
        final String cityToDeliver = voronoiService.findFastestRoute(depotsRequest, parcel.getRecipient().getCity());
        return new City(cityToDeliver);
    }

    @Override
    public City determineDeliveryDepot(ParcelUpdate parcelUpdate) {
        final List<Depot> depots = depotPort.findAll();
        final List<DepotDto> depotsRequest = depots.stream().map(this::prepareDepotRequest).toList();
        final String cityToDeliver = voronoiService.findFastestRoute(depotsRequest, parcelUpdate.getRecipientCity());
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
