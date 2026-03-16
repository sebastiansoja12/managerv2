package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import java.time.Instant;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnToken;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.enumeration.Status;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentCode;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId;

public class ReturnPackageToEntityMapper {

    public static ReturnPackageEntity map(final ReturnPackage returnPackage) {
        final ReturnId returnId = new ReturnId(returnPackage.getReturnPackageId().value());
        final ShipmentId shipmentId = new ShipmentId(returnPackage.getShipmentId().value());
        final String reason = returnPackage.getReason();
        final Status status = ReturnStatusMapper.toEntityStatus(returnPackage.getReturnStatus());
        final ReturnToken returnToken = returnPackage.getReturnToken() != null ?
                new ReturnToken(returnPackage.getReturnToken().value()) : null;
        final DepartmentCode assignedDepartmentCode = new DepartmentCode(returnPackage.getAssignedDepartmentCode().value());
        final DepartmentCode returnedDepartmentCode = new DepartmentCode(returnPackage.getReturnedDepartmentCode().value());
        final UserId assignedTo = new UserId(returnPackage.getAssignedTo().value());
        final UserId processedBy = new UserId(returnPackage.getProcessedBy().value());
        final ReasonCode reasonCode = returnPackage.getReasonCode();
        final Instant createdAt = returnPackage.getCreatedAt();
        final Instant updatedAt = returnPackage.getUpdatedAt();
		return new ReturnPackageEntity(returnId, shipmentId, reason, status, returnToken, assignedDepartmentCode,
				returnedDepartmentCode, assignedTo, processedBy, reasonCode, createdAt, updatedAt);
    }
}
