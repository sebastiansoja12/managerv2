package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import java.time.Instant;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.infrastructure.adapter.primary.api.ReturnPackageIdApi;

public record ReturnPackageApi(ReturnPackageIdApi returnPackageId, ShipmentIdApi shipmentId, String reason,
		ReturnStatusApi returnStatus, ReturnTokenApi returnToken, DepartmentId assignedDepartmentId,
		DepartmentId returnedDepartmentId, UserIdApi assignedTo, UserIdApi processedBy, ReasonCodeApi reasonCode,
		OperatorId operatorId, Instant createdAt, Instant updatedAt) {
}
