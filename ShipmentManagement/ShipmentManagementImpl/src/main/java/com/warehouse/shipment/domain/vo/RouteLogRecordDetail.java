package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.LocalDateTime;

public record RouteLogRecordDetail(
        Long id,
        TerminalId terminalId,
        String version,
        UserId userId,
        String username,
        String supplierCode,
        DepartmentId departmentId,
        String departmentCode,
        ShipmentStatus shipmentStatus,
        String description,
        LocalDateTime timestamp,
        ProcessType processType,
        String request
) {
}
