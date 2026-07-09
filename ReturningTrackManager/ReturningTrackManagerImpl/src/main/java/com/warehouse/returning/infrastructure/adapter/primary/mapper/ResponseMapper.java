package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import java.time.Instant;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.ReturnPackageIdApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.*;

public abstract class ResponseMapper {

	public ResponseMapper() {

	}

	public static ReturnResponseApi toResponseApi(final ReturnResponse res) {
		return new ReturnResponseApi(res.processReturn().stream()
				.map(processReturn -> new ProcessReturnDto(new ShipmentIdDto(processReturn.shipmentId().value()),
						new ReturnIdDto(processReturn.returnId().getValue()), processReturn.processStatus().name()))
                .toList());
	}

	public static ReturnPackageApi toResponseApi(final ReturnPackage returnPackage) {
		final ReturnPackageIdApi returnPackageId = new ReturnPackageIdApi(returnPackage.getReturnPackageId().value());
		final ShipmentIdApi shipmentId = new ShipmentIdApi(returnPackage.getShipmentId().value());
		final String reason = returnPackage.getReason();
		final ReturnStatusApi returnStatus = ReturnStatusApi.valueOf(returnPackage.getReturnStatus().name());
		final ReturnTokenApi returnToken = new ReturnTokenApi(returnPackage.getReturnToken().value());
		final DepartmentCodeApi assignedDepartmentCode = new DepartmentCodeApi(returnPackage.getAssignedDepartmentCode().value());
		final DepartmentCodeApi returnedDepartmentCode = new DepartmentCodeApi(returnPackage.getReturnedDepartmentCode().value());
		final UserIdApi assignedTo = new UserIdApi(returnPackage.getAssignedTo().value());
		final UserIdApi processedBy = new UserIdApi(returnPackage.getProcessedBy().value());
		final ReasonCodeApi reasonCode = new ReasonCodeApi(returnPackage.getReasonCode().name());
		final Instant createdAt = returnPackage.getCreatedAt();
		final Instant updatedAt = returnPackage.getUpdatedAt();
		return new ReturnPackageApi(returnPackageId, shipmentId, reason, returnStatus, returnToken,
				assignedDepartmentCode, returnedDepartmentCode, assignedTo, processedBy, reasonCode, createdAt,
				updatedAt);
	}
}
