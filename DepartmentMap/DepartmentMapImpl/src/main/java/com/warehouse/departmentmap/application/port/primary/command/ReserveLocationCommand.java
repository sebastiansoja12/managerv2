package com.warehouse.departmentmap.application.port.primary.command;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;

public record ReserveLocationCommand(PalletTrackingNumber palletTrackingNumber, String note) {
}
