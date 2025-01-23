package com.warehouse.voronoi.infrastructure.adapter.primary;

import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;

import com.warehouse.voronoi.domain.vo.VoronoiResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VoronoiServiceAdapter implements VoronoiService {

    private final VoronoiPort voronoiPort;

    @Override
    public VoronoiResponseDto findFastestRoute(final VoronoiRequestDto voronoiRequest) {
        final VoronoiRequest request = VoronoiRequest.from(voronoiRequest);
        final VoronoiResponse voronoiResponse = voronoiPort.findFastestRoute(request);
        return new VoronoiResponseDto(voronoiResponse.departmentCode().getValue(), voronoiResponse.city());
    }
}

