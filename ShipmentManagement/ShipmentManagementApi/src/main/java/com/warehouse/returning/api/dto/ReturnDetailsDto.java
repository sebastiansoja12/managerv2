package com.warehouse.returning.api.dto;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;

import com.warehouse.commonassets.enumeration.ReturnStatus;

public record ReturnDetailsDto(
        LongValueDto returnPackageId,
        LongValueDto shipmentId,
        String reason,
        ReturnStatus returnStatus,
        StringValueDto returnToken,
        DepartmentId assignedDepartmentId,
        DepartmentId returnedDepartmentId,
        StringValueDto assignedDepartmentCode,
        StringValueDto returnedDepartmentCode,
        LongValueDto assignedTo,
        LongValueDto processedBy,
        StringValueDto reasonCode,
        OperatorId operatorId,
        Instant createdAt,
        Instant updatedAt) {

}
