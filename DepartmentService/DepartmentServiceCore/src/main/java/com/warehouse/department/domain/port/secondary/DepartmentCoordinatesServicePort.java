package com.warehouse.department.domain.port.secondary;

import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.Coordinates;

public interface DepartmentCoordinatesServicePort {
    Coordinates getCoordinates(final Address address);
}
