package com.warehouse.incident.api.dto;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record DepartmentIncidentStatisticsDto(DepartmentCode departmentCode,
                                              long damagedShipments,
                                              long damageClaims) {
}
