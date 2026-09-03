package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;

public record ShipmentOwnerContext(OperatorId operatorId, DepartmentId originDepartmentId, DepartmentId targetDepartmentId) {
}
