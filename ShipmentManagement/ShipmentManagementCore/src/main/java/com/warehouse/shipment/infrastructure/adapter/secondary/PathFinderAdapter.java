package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    @Override
    public void determineDeliveryDepot(final Shipment shipment, final Address address) {
        final VoronoiRequestDto voronoiRequest = new VoronoiRequestDto(
                address.getCity(), address.getPostalCode()
        );
        final VoronoiResponseDto voronoiResponse = voronoiService.findFastestRoute(voronoiRequest);
        shipment.changeDestinationDepartment(voronoiResponse.departmentCode());
    }
}
