package com.warehouse.pallet.domain.enumeration;

import java.util.EnumSet;
import java.util.Set;

public enum PalletStatus {
    CREATED,
    PREPARING,
    NOT_READY,
    READY,
    LOADED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED,
    @Deprecated
    EMPTY;

    public boolean canTransitionTo(final PalletStatus targetStatus) {
        return allowedTransitions().contains(targetStatus);
    }

    public boolean isContentModifiable() {
        return this == CREATED || this == PREPARING || this == NOT_READY || this == READY;
    }

    public boolean isManuallySettable() {
        return this == IN_TRANSIT || this == DELIVERED;
    }

    public boolean isFinished() {
        return this == DELIVERED || this == CANCELLED;
    }

    private Set<PalletStatus> allowedTransitions() {
        return switch (this) {
            case CREATED -> EnumSet.of(PREPARING, NOT_READY, READY, CANCELLED);
            case PREPARING -> EnumSet.of(PREPARING, NOT_READY, READY, CANCELLED);
            case NOT_READY -> EnumSet.of(PREPARING, NOT_READY, READY, CANCELLED);
            case READY -> EnumSet.of(PREPARING, NOT_READY, READY, LOADED, CANCELLED);
            case LOADED -> EnumSet.of(IN_TRANSIT);
            case IN_TRANSIT -> EnumSet.of(DELIVERED);
            case DELIVERED, CANCELLED -> EnumSet.noneOf(PalletStatus.class);
            case EMPTY -> EnumSet.of(PREPARING, NOT_READY, READY, CANCELLED);
        };
    }
}
