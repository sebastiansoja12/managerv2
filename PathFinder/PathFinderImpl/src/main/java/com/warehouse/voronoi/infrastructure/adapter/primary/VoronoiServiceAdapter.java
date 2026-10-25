package com.warehouse.voronoi.infrastructure.adapter.primary;

import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.application.port.primary.VoronoiPort;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;
import com.warehouse.voronoi.dto.DepartmentCodeDto;
import com.warehouse.voronoi.domain.model.Department;


public class VoronoiServiceAdapter implements VoronoiService {

    private final VoronoiPort voronoiPort;

    public VoronoiServiceAdapter(final VoronoiPort voronoiPort) {
        this.voronoiPort = voronoiPort;
    }

    @Override
    public VoronoiResponseDto findFastestRoute(final VoronoiRequestDto voronoiRequest) {
        final VoronoiRequest request = new VoronoiRequest(voronoiRequest.city(),
                new Coordinates(voronoiRequest.coordinatesOfGivenLocation().latitude(), voronoiRequest.coordinatesOfGivenLocation().longitude()),
                voronoiRequest.departments().stream()
                        .map(department -> new Department(department.city(), department.street(),
                                department.country(), department.departmentCode(),
                                new Coordinates(department.coordinates().latitude(), department.coordinates().longitude())))
                        .toList());
        final VoronoiResponse voronoiResponse = voronoiPort.findFastestRoute(request);
        return new VoronoiResponseDto(
                new DepartmentCodeDto(voronoiResponse.departmentCode().getValue()), voronoiResponse.city());
    }

}
