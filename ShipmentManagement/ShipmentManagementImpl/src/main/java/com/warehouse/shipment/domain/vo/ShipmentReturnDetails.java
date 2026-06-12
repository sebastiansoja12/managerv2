package com.warehouse.shipment.domain.vo;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;

public record ShipmentReturnDetails(
        ReturnId returnPackageId,
        ShipmentId shipmentId,
        String reason,
        ReturnStatus returnStatus,
        String returnToken,
        DepartmentCode assignedDepartmentCode,
        DepartmentCode returnedDepartmentCode,
        UserId assignedTo,
        UserId processedBy,
        ReasonCode reasonCode,
        Long operatorId,
        Instant createdAt,
        Instant updatedAt) {
}
