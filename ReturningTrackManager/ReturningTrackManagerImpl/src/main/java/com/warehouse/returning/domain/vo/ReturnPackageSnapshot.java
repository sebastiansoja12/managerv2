package com.warehouse.returning.domain.vo;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnStatus;

import java.time.Instant;

public record ReturnPackageSnapshot(
        ReturnPackageId returnPackageId,
        ShipmentId shipmentId,
        String reason,
        ReturnStatus returnStatus,
        ReturnToken returnToken,
        DepartmentId assignedDepartmentId,
        DepartmentId returnedDepartmentId,
        UserId assignedTo,
        UserId processedBy,
        ReasonCode reasonCode,
        OperatorId operatorId,
        Instant createdAt,
        Instant updatedAt
) {}
