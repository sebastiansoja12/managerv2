package com.warehouse.palletmanagement.api.event;

public final class PalletTopics {

    public static final String LOADED = "pallet.loaded";
    public static final String LOADING_REVERTED = "pallet.loading-reverted";
    public static final String UNLOADED = "pallet.unloaded";
    public static final String DEPARTMENTS_CHANGED = "pallet.departments-changed";

    private PalletTopics() {
    }
}
