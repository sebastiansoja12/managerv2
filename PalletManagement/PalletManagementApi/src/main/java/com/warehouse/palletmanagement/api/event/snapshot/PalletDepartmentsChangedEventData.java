package com.warehouse.palletmanagement.api.event.snapshot;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;

public record PalletDepartmentsChangedEventData(PalletId palletId, PalletTrackingNumber trackingNumber,
                                                DepartmentId originDepartmentId,
                                                DepartmentId destinationDepartmentId) {
}
