package com.warehouse.logistics.application.port.primary.result;

import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;

import java.util.List;

public record PalletUnloadOutcome(PalletTrackingNumber trackingNumber,
                                  PalletId palletId,
                                  String palletStatus,
                                  boolean success,
                                  boolean alreadyUnloaded,
                                  String errorCode,
                                  String errorMessage,
                                  List<PalletScanDetail> details) {
}
