package com.warehouse.palletmanagement.application.port.primary;

import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.application.port.primary.command.PalletScanCommand;
import com.warehouse.palletmanagement.application.port.primary.command.PalletUnloadScanCommand;
import com.warehouse.palletmanagement.application.port.primary.command.UnloadPalletCommand;
import com.warehouse.palletmanagement.application.port.primary.result.PalletScanResult;
import com.warehouse.palletmanagement.application.port.primary.result.PalletUnloadResult;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;

public interface PalletScanPort {
    PalletScanResult scan(final PalletScanCommand command);

    PalletDetails revertLoading(final PalletId palletId);

    PalletDetails unload(final UnloadPalletCommand command);

    PalletUnloadResult unloadScanned(final PalletUnloadScanCommand command);
}
