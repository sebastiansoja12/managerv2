package com.warehouse.fleettracking.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.fleettracking.domain.vo.TrackingDriver;

import java.util.Optional;

public interface TrackingDriverServicePort {

    Optional<TrackingDriver> currentDriver();

    Optional<DepartmentCode> currentDepartmentCode();
}
