package com.warehouse.palletmanagement.api.dto;

import java.util.List;

public record PalletScanResponseDto(String palletId,
                                    String trackingNumber,
                                    String palletStatus,
                                    boolean success,
                                    boolean alreadyLoaded,
                                    String errorCode,
                                    String errorMessage,
                                    List<PalletScanShipmentResultDto> shipments) {
}
