package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.logistics.domain.vo.PalletScanCommand;
import com.warehouse.logistics.domain.vo.PalletScanOutcome;

public interface PalletScanServicePort {
    PalletScanOutcome scan(final PalletScanCommand command);
}
