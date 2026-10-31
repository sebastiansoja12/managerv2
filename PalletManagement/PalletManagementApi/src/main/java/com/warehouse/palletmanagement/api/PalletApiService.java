package com.warehouse.palletmanagement.api;

import com.warehouse.palletmanagement.api.dto.PalletScanRequestDto;
import com.warehouse.palletmanagement.api.dto.PalletScanResponseDto;
import com.warehouse.palletmanagement.api.dto.PalletUnloadRequestDto;
import com.warehouse.palletmanagement.api.dto.PalletUnloadResponseDto;

public interface PalletApiService {
    PalletScanResponseDto scan(final PalletScanRequestDto palletScanRequest);

    PalletUnloadResponseDto unload(final PalletUnloadRequestDto palletUnloadRequest);
}
