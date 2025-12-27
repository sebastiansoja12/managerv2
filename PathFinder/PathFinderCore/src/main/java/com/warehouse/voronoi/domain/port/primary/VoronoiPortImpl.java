package com.warehouse.voronoi.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.exception.MissingDepotsException;
import com.warehouse.voronoi.domain.exception.MissingRequestCityException;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoronoiPortImpl implements VoronoiPort {

    private final ComputeService computeService;

    private final VoronoiServicePort voronoiServicePort;

    public VoronoiPortImpl(final ComputeService computeService,
                           final VoronoiServicePort voronoiServicePort) {
        this.computeService = computeService;
        this.voronoiServicePort = voronoiServicePort;
    }

    @Override
    public VoronoiResponse findFastestRoute(final VoronoiRequest request) {
        log.info("Finding nearest department for recipient city {}", request.getCity());
        final List<Department> departments = request.getDepartments();
        validateRequest(departments, request.getCity());
        final DepartmentCode departmentCode = computeService.calculateDepartmentCode(request, departments);
        log.info("Nearest department for recipient city {} is {}", request.getCity(), departmentCode.getValue());
        return new VoronoiResponse(departmentCode, request.getCity());
    }

    @Override
    public Coordinates obtainCoordinates(final String requestCity) {
        return voronoiServicePort.obtainCoordinates(requestCity);
    }

    private void validateRequest(final List<Department> departments, final String requestCity) {
        if (departments.isEmpty()) {
            throw new MissingDepotsException("Departments cannot be empty");
        } else if (requestCity == null) {
            throw new MissingRequestCityException("Request city is empty");
        }
    }
}
