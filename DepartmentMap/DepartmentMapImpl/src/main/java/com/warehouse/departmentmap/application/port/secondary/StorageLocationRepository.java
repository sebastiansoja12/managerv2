package com.warehouse.departmentmap.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.departmentmap.domain.model.StorageLocation;

import java.util.List;
import java.util.Optional;

public interface StorageLocationRepository {
    StorageLocation save(final StorageLocation storageLocation);
    Optional<StorageLocation> findById(final MapElementId locationId);
    List<StorageLocation> findByMap(final DepartmentMapId departmentMapId);
    Optional<StorageLocation> findByPallet(final PalletTrackingNumber palletTrackingNumber);
    List<StorageLocation> findByPalletNumberFragment(final String fragment, final int limit);
    void delete(final StorageLocation storageLocation);
}
