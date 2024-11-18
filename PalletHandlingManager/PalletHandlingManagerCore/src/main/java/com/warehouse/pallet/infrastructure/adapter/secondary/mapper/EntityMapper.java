package com.warehouse.pallet.infrastructure.adapter.secondary.mapper;

import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.infrastructure.adapter.secondary.entity.PalletEntity;

public interface EntityMapper {
    PalletEntity map(final Pallet pallet);
}
