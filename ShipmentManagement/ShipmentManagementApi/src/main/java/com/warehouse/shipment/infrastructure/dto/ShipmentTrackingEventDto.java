package com.warehouse.shipment.infrastructure.dto;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ProcessId;

public record ShipmentTrackingEventDto(LocalDateTime timestamp,
                                       ShipmentStatus status,
                                       ProcessType processType,
                                       ProcessId processId,
                                       String departmentCode,
                                       String username,
                                       String description) {
}
