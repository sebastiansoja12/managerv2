package com.warehouse.palletmanagement.api;

import com.warehouse.palletmanagement.api.dto.PalletScanRequestDto;
import com.warehouse.palletmanagement.api.dto.PalletScanResponseDto;

public interface PalletApiService {
    PalletScanResponseDto scan(final PalletScanRequestDto palletScanRequest);
}
