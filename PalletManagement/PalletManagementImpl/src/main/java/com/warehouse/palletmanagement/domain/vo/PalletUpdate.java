package com.warehouse.palletmanagement.domain.vo;

public record PalletUpdate(String palletType,
                           String handlingPriority,
                           Boolean refrigerated,
                           PalletDimension dimension,
                           String sealNumber) {

    public PalletUpdate {
        if (dimension != null && (dimension.length() <= 0 || dimension.width() <= 0 || dimension.height() <= 0)) {
            throw new IllegalArgumentException("Pallet dimensions must be positive");
        }
    }
}
