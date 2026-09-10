package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.configuration.identificator.OperatorId;

public record PalletSearchCriteria(OperatorId operatorId, PalletStatus palletStatus, boolean onlyEmpty,
                                   int page, int size) {

    public PalletSearchCriteria {
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }
        if (size <= 0 || size > 200) {
            throw new IllegalArgumentException("Page size must be between 1 and 200");
        }
    }
}
