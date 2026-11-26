package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;

public record PalletDepartmentsChangedSnapshot(PalletId palletId, PalletTrackingNumber trackingNumber,
                                               DepartmentId originDepartmentId,
                                               DepartmentId destinationDepartmentId) {
}
