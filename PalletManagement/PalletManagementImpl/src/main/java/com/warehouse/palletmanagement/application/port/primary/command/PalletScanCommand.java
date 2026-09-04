package com.warehouse.palletmanagement.application.port.primary.command;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;

public record PalletScanCommand(PalletTrackingNumber trackingNumber) {
}
