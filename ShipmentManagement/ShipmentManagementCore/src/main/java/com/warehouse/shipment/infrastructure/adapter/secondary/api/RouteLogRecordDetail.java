package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;

import lombok.Builder;

@Builder
public record RouteLogRecordDetail(
        Long id,
        TerminalId terminalId,
        String version,
        String username,
        String supplierCode,
        String departmentCode,
        ShipmentStatus shipmentStatus,
        String description,
        LocalDateTime timestamp,
        ProcessType processType,
        String request
) {}