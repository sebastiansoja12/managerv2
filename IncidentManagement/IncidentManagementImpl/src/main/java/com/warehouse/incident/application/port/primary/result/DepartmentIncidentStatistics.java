package com.warehouse.incident.application.port.primary.result;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record DepartmentIncidentStatistics(DepartmentCode departmentCode,
                                           long damagedShipments,
                                           long damageClaims) {
}
