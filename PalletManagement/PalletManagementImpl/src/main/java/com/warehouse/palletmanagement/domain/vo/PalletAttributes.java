package com.warehouse.palletmanagement.domain.vo;

public record PalletAttributes(String palletType,
                               String handlingPriority,
                               boolean refrigerated,
                               PalletDimension dimension) {

    public static final PalletAttributes DEFAULT = new PalletAttributes(null, null, false, null);
}
