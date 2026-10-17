package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnToken;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.enumeration.Status;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.OperatorId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId;

import java.time.Instant;

public class ReturnPackageToEntityMapper {

    public static ReturnPackageEntity map(final ReturnPackage returnPackage) {
        final ReturnId returnId = new ReturnId(returnPackage.getReturnPackageId().value());
        final ShipmentId shipmentId = new ShipmentId(returnPackage.getShipmentId().value());
        final String reason = returnPackage.getReason();
        final Status status = ReturnStatusMapper.toEntityStatus(returnPackage.getReturnStatus());
        final ReturnToken returnToken = returnPackage.getReturnToken() != null ?
                new ReturnToken(returnPackage.getReturnToken().value()) : null;
        final DepartmentId assignedDepartmentId = returnPackage.getAssignedDepartmentId() == null ? null
                : new DepartmentId(returnPackage.getAssignedDepartmentId().value());
        final DepartmentId returnedDepartmentId = returnPackage.getReturnedDepartmentId() == null ? null
                : new DepartmentId(returnPackage.getReturnedDepartmentId().value());
        final UserId assignedTo = new UserId(returnPackage.getAssignedTo().value());
        final UserId processedBy = returnPackage.getProcessedBy() == null ? null
                : new UserId(returnPackage.getProcessedBy().value());
        final OperatorId operatorId = returnPackage.getOperatorId() == null ? null
                : new OperatorId(returnPackage.getOperatorId().value());
        final ReasonCode reasonCode = returnPackage.getReasonCode();
        final Instant createdAt = returnPackage.getCreatedAt();
        final Instant updatedAt = returnPackage.getUpdatedAt();
		return new ReturnPackageEntity(returnId, shipmentId, reason, status, returnToken, assignedDepartmentId,
				returnedDepartmentId, assignedTo, processedBy, reasonCode, operatorId,
                createdAt, updatedAt);
    }
}
