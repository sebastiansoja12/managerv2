package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.dto.CoordinatesDto;
import com.warehouse.voronoi.dto.DepartmentDto;


public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    private final DepartmentApiService departmentApiService;

    public PathFinderAdapter(final VoronoiService voronoiService,
                             final DepartmentApiService departmentApiService) {
        this.voronoiService = voronoiService;
        this.departmentApiService = departmentApiService;
    }

    @Override
    public Result<VoronoiResponse, ErrorCode> determineDeliveryDepartment(final Address address) {
        final List<DepartmentDto> departments = departmentApiService.getAllDepartments()
                .stream()
                .map(dep -> new DepartmentDto(dep.departmentCode(),
                        dep.city(), dep.street(), dep.zipCode(), dep.country(),
                        new CoordinatesDto(dep.coordinates().latitude(), dep.coordinates().longitude())))
                .toList();
        final VoronoiRequestDto voronoiRequest = new VoronoiRequestDto(
                address.getCity(), address.getPostalCode(), departments
        );
        final VoronoiResponseDto voronoiResponse = voronoiService.findFastestRoute(voronoiRequest);
        return Result.success(new VoronoiResponse(voronoiResponse.departmentCode()));
    }
}
