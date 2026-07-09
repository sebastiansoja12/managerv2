package com.warehouse.returning.domain.vo;

import java.time.Instant;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnStatus;

public record ReturnPackageSnapshot(
        ReturnPackageId returnPackageId,
        ShipmentId shipmentId,
        String reason,
        ReturnStatus returnStatus,
        ReturnToken returnToken,
        DepartmentCode assignedDepartmentCode,
        DepartmentCode returnedDepartmentCode,
        UserId assignedTo,
        UserId processedBy,
        ReasonCode reasonCode,
        Instant createdAt,
        Instant updatedAt
) {}
