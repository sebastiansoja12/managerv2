package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import java.time.Instant;

import com.warehouse.returning.infrastructure.adapter.primary.api.ReturnPackageIdApi;

public record ReturnPackageApi(ReturnPackageIdApi returnPackageId, ShipmentIdApi shipmentId, String reason,
		ReturnStatusApi returnStatus, ReturnTokenApi returnToken, DepartmentCodeApi assignedDepartmentCode,
		DepartmentCodeApi returnedDepartmentCode, UserIdApi assignedTo, UserIdApi processedBy, ReasonCodeApi reasonCode,
		Instant createdAt, Instant updatedAt) {
}
