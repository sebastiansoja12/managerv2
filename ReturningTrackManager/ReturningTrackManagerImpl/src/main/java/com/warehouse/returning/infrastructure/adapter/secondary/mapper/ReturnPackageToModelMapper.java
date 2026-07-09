package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;

public class ReturnPackageToModelMapper {


    public static ReturnPackage map(final ReturnPackageEntity entity) {
        return ReturnPackage.builder()
                .returnPackageId(ReturnPackageId.from(entity.getReturnId()))
                .shipmentId(new ShipmentId(entity.getShipmentId().getValue()))
                .reason(entity.getReason())
                .returnStatus(ReturnStatusMapper.toModelStatus(entity.getReturnStatus()))
                .returnToken(entity.getReturnToken() != null ? new ReturnToken(entity.getReturnToken().getValue()) : null)
                .assignedDepartmentCode(new com.warehouse.returning.domain.vo.DepartmentCode(entity.getAssignedDepartmentCode().getValue()))
                .returnedDepartmentCode(new com.warehouse.returning.domain.vo.DepartmentCode(entity.getReturnedDepartmentCode().getValue()))
                .assignedTo(new com.warehouse.returning.domain.vo.UserId(entity.getAssignedTo().getValue()))
                .processedBy(new com.warehouse.returning.domain.vo.UserId(entity.getProcessedBy().getValue()))
                .reasonCode(entity.getReasonCode())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
