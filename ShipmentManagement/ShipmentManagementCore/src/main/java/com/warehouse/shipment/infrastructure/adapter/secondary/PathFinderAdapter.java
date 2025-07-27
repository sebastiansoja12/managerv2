package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    @Override
    public VoronoiResponse determineDeliveryDepartment(final Address address) {
        final VoronoiRequestDto voronoiRequest = new VoronoiRequestDto(
                address.getCity(), address.getPostalCode()
        );
        final VoronoiResponseDto voronoiResponse = voronoiService.findFastestRoute(voronoiRequest);
        return new VoronoiResponse(voronoiResponse.departmentCode());
    }
}
