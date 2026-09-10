package com.warehouse.departmentmap.application.port.secondary;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.departmentmap.domain.model.PalletMovement;

import java.util.List;

public interface PalletMovementRepository {
    void save(final PalletMovement palletMovement);
    List<PalletMovement> findHistory(final PalletTrackingNumber palletTrackingNumber);
}
