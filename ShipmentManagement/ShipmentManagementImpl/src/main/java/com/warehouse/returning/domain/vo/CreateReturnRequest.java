package com.warehouse.returning.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.domain.enumeration.ReasonCode;

public record CreateReturnRequest(ShipmentId shipmentId,
                                  String reason,
                                  DepartmentId departmentId,
                                  UserId assignedTo,
                                  ReasonCode reasonCode) {
}
