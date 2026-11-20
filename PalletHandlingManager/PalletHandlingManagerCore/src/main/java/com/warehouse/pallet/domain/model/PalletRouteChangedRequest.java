package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;

public record PalletRouteChangedRequest(String eventId, PalletId palletId, DepartmentId originDepartmentId,
                                        DepartmentId destinationDepartmentId) {
}
