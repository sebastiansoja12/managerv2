package com.warehouse.departmentmap.api;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.departmentmap.api.dto.PalletLocationDto;

import java.util.Optional;

public interface PalletLocationApiService {
    Optional<PalletLocationDto> findPalletLocation(final PalletTrackingNumber palletTrackingNumber);
}
