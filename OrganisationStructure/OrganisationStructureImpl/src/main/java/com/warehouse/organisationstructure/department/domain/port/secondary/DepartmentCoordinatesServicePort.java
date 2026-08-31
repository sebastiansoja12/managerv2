package com.warehouse.organisationstructure.department.domain.port.secondary;

import com.warehouse.organisationstructure.department.domain.vo.Address;
import com.warehouse.organisationstructure.department.domain.vo.Coordinates;

public interface DepartmentCoordinatesServicePort {
    Coordinates getCoordinates(final Address address);
}
