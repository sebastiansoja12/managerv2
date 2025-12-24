package com.warehouse.voronoi.infrastructure.adapter.primary;

import com.warehouse.voronoi.VoronoiCoordinatesService;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;
import com.warehouse.voronoi.dto.CoordinatesDto;


public class VoronoiServiceAdapter implements VoronoiService, VoronoiCoordinatesService {

    private final VoronoiPort voronoiPort;

    public VoronoiServiceAdapter(final VoronoiPort voronoiPort) {
        this.voronoiPort = voronoiPort;
    }

    @Override
    public VoronoiResponseDto findFastestRoute(final VoronoiRequestDto voronoiRequest) {
        final VoronoiRequest request = VoronoiRequest.from(voronoiRequest);
        final VoronoiResponse voronoiResponse = voronoiPort.findFastestRoute(request);
        return new VoronoiResponseDto(voronoiResponse.departmentCode().getValue(), voronoiResponse.city());
    }

    @Override
    public CoordinatesDto findCoordinates(final VoronoiRequestDto voronoiRequest) {
        final VoronoiRequest request = VoronoiRequest.from(voronoiRequest);
        final Coordinates coordinates = voronoiPort.obtainCoordinates(request.getCity());
        return new CoordinatesDto(coordinates.lat(), coordinates.lon());
    }
}

