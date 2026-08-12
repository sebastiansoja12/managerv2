package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;

public record RouteLogRecordDetailDto(
        Long id,
        TerminalIdDto terminalId,
        String version,
        UserIdDto userId,
        String supplierCode,
        DepartmentIdDto departmentId,
        ShipmentStatus shipmentStatus,
        String description,
        LocalDateTime timestamp,
        ProcessType processType,
        String request
) {
}
