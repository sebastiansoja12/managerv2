package com.warehouse.departmentmap.application.port.primary.result;

import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.PalletPlacement;
import com.warehouse.departmentmap.domain.model.StorageLocation;

public record PalletLocationResult(PalletPlacement placement,
                                   StorageLocation location,
                                   DepartmentMap departmentMap) {
}
