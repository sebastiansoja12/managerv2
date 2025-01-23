package com.warehouse.pallet.infrastructure.adapter.secondary.mapper;

import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletDocument;

public interface EntityMapper {
    PalletDocument map(final Pallet pallet);
}
