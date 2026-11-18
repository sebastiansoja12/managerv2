package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.Optional;

public interface CurrentDepartmentCodePort {

    Optional<DepartmentCode> currentDepartmentCode();
}
