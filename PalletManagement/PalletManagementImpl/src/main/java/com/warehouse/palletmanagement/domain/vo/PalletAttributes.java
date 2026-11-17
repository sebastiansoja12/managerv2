package com.warehouse.palletmanagement.domain.vo;

public record PalletAttributes(String palletType,
                               String handlingPriority,
                               boolean refrigerated,
                               boolean mixedDestinationsAllowed,
                               PalletDimension dimension) {

    public static final PalletAttributes DEFAULT = new PalletAttributes(null, null, false, false, null);
}
