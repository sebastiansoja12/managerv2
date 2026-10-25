package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.geocoding.api.GeocodingAddressDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.OutputResponseMapper;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.dto.AddressDto;
import com.warehouse.voronoi.dto.CoordinatesDto;
import com.warehouse.voronoi.dto.DepartmentDto;

import java.util.List;


public class PathFinderAdapter implements PathFinderServicePort {

    private final VoronoiService voronoiService;

    private final DepartmentApiService departmentApiService;
    private final GeocodingCoordinatesService geocodingCoordinatesService;

    public PathFinderAdapter(final VoronoiService voronoiService,
                             final DepartmentApiService departmentApiService,
                             final GeocodingCoordinatesService geocodingCoordinatesService) {
        this.voronoiService = voronoiService;
        this.departmentApiService = departmentApiService;
        this.geocodingCoordinatesService = geocodingCoordinatesService;
    }

    @Override
    public Result<VoronoiResponse, ErrorCode> determineDeliveryDepartment(final Address address) {
        final List<DepartmentDto> departments = departmentApiService.getAllDepartments()
                .stream()
                .map(dep -> new DepartmentDto(dep.departmentCode(),
                        dep.city(), dep.street(), dep.zipCode(), dep.countryCode(),
                        new CoordinatesDto(dep.coordinates().latitude(), dep.coordinates().longitude())))
                .toList();
        final GeocodingCoordinatesDto coordinates = geocodingCoordinatesService.findCoordinates(
                new GeocodingAddressDto(address.getCity(), address.getStreet(), address.getPostalCode()));
        final VoronoiRequestDto voronoiRequest = new VoronoiRequestDto(mapAddress(address),
                new CoordinatesDto(coordinates.latitude(), coordinates.longitude()), departments);
        final VoronoiResponseDto voronoiResponse = voronoiService.findFastestRoute(voronoiRequest);
        return Result.success(OutputResponseMapper.map(voronoiResponse));
    }

    private AddressDto mapAddress(final Address address) {
        return new AddressDto(address.getCity(), address.getStreet(), address.getPostalCode(), address.getCountryCode());
    }
}
