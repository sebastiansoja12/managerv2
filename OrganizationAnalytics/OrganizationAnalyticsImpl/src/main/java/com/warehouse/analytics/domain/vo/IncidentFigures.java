package com.warehouse.analytics.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record IncidentFigures(DepartmentCode departmentCode, long damagedShipments, long damageClaims) {
}
