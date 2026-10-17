package com.warehouse.returning.domain.vo;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.UUID;

public record ReturnState(ReturnPackageId returnId,
                          ShipmentId originalShipmentId,
                          ReturnStatus status,
                          UUID pickupId,
                          ShipmentId returnShipmentId,
                          DepartmentId scanDepartmentId,
                          long version,
                          DepartmentId assignedDepartmentId,
                          DepartmentId returnedDepartmentId) {
}
