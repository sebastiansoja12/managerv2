package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;

public class ReturnPackageToModelMapper {


    public static ReturnPackage map(final ReturnPackageEntity entity) {
        return ReturnPackage.builder()
                .returnPackageId(new ReturnPackageId(entity.getReturnId().getValue()))
                .shipmentId(new ShipmentId(entity.getShipmentId().getValue()))
                .reason(entity.getReason())
                .returnStatus(ReturnStatusMapper.toModelStatus(entity.getReturnStatus()))
                .returnToken(entity.getReturnToken() != null ? new ReturnToken(entity.getReturnToken().getValue()) : null)
                .assignedDepartmentId(entity.getAssignedDepartmentId() == null ? null
                        : new DepartmentId(entity.getAssignedDepartmentId().getValue()))
                .returnedDepartmentId(entity.getReturnedDepartmentId() == null ? null
                        : new DepartmentId(entity.getReturnedDepartmentId().getValue()))
                .assignedTo(new com.warehouse.returning.domain.vo.UserId(entity.getAssignedTo().getValue()))
                .processedBy(entity.getProcessedBy() == null ? null
                        : new com.warehouse.returning.domain.vo.UserId(entity.getProcessedBy().getValue()))
                .reasonCode(entity.getReasonCode())
                .operatorId(entity.getOperatorId() == null ? null
                        : new OperatorId(entity.getOperatorId().getValue()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
