package com.warehouse.voronoi.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.exception.MissingCoordinatesException;
import com.warehouse.voronoi.domain.exception.MissingDepotsException;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class VoronoiPortImpl implements VoronoiPort {

    private final ComputeService computeService;

    public VoronoiPortImpl(final ComputeService computeService) {
        this.computeService = computeService;
    }

    @Override
    public VoronoiResponse findFastestRoute(final VoronoiRequest request) {
        log.info("Finding nearest department for recipient city {}", request.getCity());
        final List<Department> departments = request.getDepartments();
        validateRequest(departments, request.getCoordinates());
        final DepartmentCode departmentCode = computeService.calculateNearestDepartment(request, departments);
        log.info("Nearest department for recipient city {} is {}", request.getCity(), departmentCode.getValue());
        return new VoronoiResponse(departmentCode, request.getCity());
    }

    private void validateRequest(final List<Department> departments, final Coordinates coordinates) {
        if (departments.isEmpty()) {
            throw new MissingDepotsException("Departments cannot be empty");
        } else if (coordinates == null) {
            throw new MissingCoordinatesException("Request coordinatesOfGivenLocation are empty");
        }
    }
}
