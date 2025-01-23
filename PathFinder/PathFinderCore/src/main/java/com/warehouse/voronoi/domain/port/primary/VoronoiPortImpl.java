package com.warehouse.voronoi.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.exception.MissingDepotsException;
import com.warehouse.voronoi.domain.exception.MissingRequestCityException;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;
import com.warehouse.voronoi.domain.port.secondary.DepotServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.vo.VoronoiResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VoronoiPortImpl implements VoronoiPort {

    private final DepotServicePort depotServicePort;

    private final ComputeService computeService;

    @Override
    public VoronoiResponse findFastestRoute(final VoronoiRequest request) {
        final List<Department> departments = depotServicePort.downloadDepots();
        validateRequest(departments, request.getCity());
        final DepartmentCode departmentCode = computeService.calculateDepartmentCode(request, departments);
        return new VoronoiResponse(departmentCode, request.getCity());
    }

    private void validateRequest(final List<Department> departments, final String requestCity) {
        if (departments.isEmpty()) {
            throw new MissingDepotsException("Depots cannot be empty");
        } else if (requestCity == null) {
            throw new MissingRequestCityException("Request city is empty");
        }
    }
}
