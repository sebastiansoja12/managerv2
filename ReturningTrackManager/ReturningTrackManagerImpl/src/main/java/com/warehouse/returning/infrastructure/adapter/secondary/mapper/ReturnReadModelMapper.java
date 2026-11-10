package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnReadModelEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;

public final class ReturnReadModelMapper {

    private ReturnReadModelMapper() {
    }

    public static ReturnReadModelEntity map(final ReturnPackageSnapshot snapshot) {
        return new ReturnReadModelEntity(
                new ReturnId(snapshot.returnPackageId().value()),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId(
                        snapshot.shipmentId().value()),
                snapshot.returnShipmentId() == null ? null
                        : new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId(
                                snapshot.returnShipmentId().value()),
                snapshot.reason(),
                ReturnStatusMapper.toEntityStatus(snapshot.returnStatus()),
                snapshot.returnToken() == null ? null
                        : new com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnToken(
                                snapshot.returnToken().value()),
                snapshot.assignedDepartmentId() == null ? null
                        : new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentId(
                                snapshot.assignedDepartmentId().value()),
                snapshot.returnedDepartmentId() == null ? null
                        : new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentId(
                                snapshot.returnedDepartmentId().value()),
                snapshot.assignedTo() == null ? null
                        : new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId(
                                snapshot.assignedTo().value()),
                snapshot.processedBy() == null ? null
                        : new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId(
                                snapshot.processedBy().value()),
                snapshot.reasonCode(),
                snapshot.operatorId() == null ? null
                        : new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.OperatorId(
                                snapshot.operatorId().value()),
                snapshot.createdAt(),
                snapshot.updatedAt());
    }

    public static ReturnPackage map(final ReturnReadModelEntity entity) {
        return ReturnPackage.builder()
                .returnPackageId(new ReturnPackageId(entity.getReturnId().getValue()))
                .shipmentId(new ShipmentId(entity.getShipmentId().getValue()))
                .returnShipmentId(entity.getReturnShipmentId() == null ? null
                        : new ShipmentId(entity.getReturnShipmentId().getValue()))
                .reason(entity.getReason())
                .returnStatus(ReturnStatusMapper.toModelStatus(entity.getReturnStatus()))
                .returnToken(entity.getReturnToken() == null ? null : new ReturnToken(entity.getReturnToken().getValue()))
                .assignedDepartmentId(entity.getAssignedDepartmentId() == null ? null
                        : new DepartmentId(entity.getAssignedDepartmentId().getValue()))
                .returnedDepartmentId(entity.getReturnedDepartmentId() == null ? null
                        : new DepartmentId(entity.getReturnedDepartmentId().getValue()))
                .assignedTo(entity.getAssignedTo() == null ? null : new UserId(entity.getAssignedTo().getValue()))
                .processedBy(entity.getProcessedBy() == null ? null : new UserId(entity.getProcessedBy().getValue()))
                .reasonCode(entity.getReasonCode())
                .operatorId(entity.getOperatorId() == null ? null : new OperatorId(entity.getOperatorId().getValue()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
