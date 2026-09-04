package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;

import java.util.Optional;

public record PalletSearchCriteria(PalletStatus palletStatus, boolean onlyEmpty, int page, int size) {

    public PalletSearchCriteria {
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }
        if (size <= 0 || size > 200) {
            throw new IllegalArgumentException("Page size must be between 1 and 200");
        }
    }

    public static PalletSearchCriteria cancelled(final int page, final int size) {
        return new PalletSearchCriteria(PalletStatus.CANCELLED, false, page, size);
    }

    public static PalletSearchCriteria empty(final int page, final int size) {
        return new PalletSearchCriteria(null, true, page, size);
    }

    public Optional<PalletStatus> status() {
        return Optional.ofNullable(palletStatus);
    }
}
