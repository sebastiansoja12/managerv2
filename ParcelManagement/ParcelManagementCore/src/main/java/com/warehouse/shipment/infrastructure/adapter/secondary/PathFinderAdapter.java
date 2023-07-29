package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.depot.api.DepotService;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.voronoi.VoronoiService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    private final DepotService depotService;

    @Override
    public City determineDeliveryDepot(ShipmentParcel parcel) {
        final List<DepotDto> depots = depotService.findAll();
        final String cityToDeliver = voronoiService.findFastestRoute(depots, parcel.getRecipient().getCity());
        return new City(cityToDeliver);
    }

    @Override
    public City determineDeliveryDepot(ParcelUpdate parcelUpdate) {
        final List<DepotDto> depots = depotService.findAll();
        final String cityToDeliver = voronoiService.findFastestRoute(depots, parcelUpdate.getRecipientCity());
        return new City(cityToDeliver);
    }
}
