package com.warehouse.logistics.domain.vo;

import java.util.List;

public record PalletScanOutcome(String trackingNumber,
                                String palletId,
                                String palletStatus,
                                boolean success,
                                boolean alreadyLoaded,
                                String errorCode,
                                String errorMessage,
                                List<PalletScanDetail> details) {
}
