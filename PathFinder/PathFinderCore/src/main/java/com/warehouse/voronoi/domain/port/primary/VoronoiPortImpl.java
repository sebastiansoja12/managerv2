package com.warehouse.voronoi.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.exception.MissingDepotsException;
import com.warehouse.voronoi.domain.exception.MissingRequestCityException;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.secondary.DepartmentServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoronoiPortImpl implements VoronoiPort {

    private final DepartmentServicePort departmentServicePort;

    private final ComputeService computeService;

    public VoronoiPortImpl(final DepartmentServicePort departmentServicePort, final ComputeService computeService) {
        this.departmentServicePort = departmentServicePort;
        this.computeService = computeService;
    }

    @Override
    public VoronoiResponse findFastestRoute(final VoronoiRequest request) {
        log.info("Finding nearest department for recipient city {}", request.getCity());
        final List<Department> departments = departmentServicePort.downloadDepartments();
        validateRequest(departments, request.getCity());
        final DepartmentCode departmentCode = computeService.calculateDepartmentCode(request, departments);
        log.info("Nearest department for recipient city {} is {}", request.getCity(), departmentCode.getValue());
        return new VoronoiResponse(departmentCode, request.getCity());
    }

    private void validateRequest(final List<Department> departments, final String requestCity) {
        if (departments.isEmpty()) {
            throw new MissingDepotsException("Departments cannot be empty");
        } else if (requestCity == null) {
            throw new MissingRequestCityException("Request city is empty");
        }
    }
}
