package com.warehouse.pallet.infrastructure.adapter.secondary.mapper;

import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.infrastructure.adapter.secondary.entity.PalletEntity;


public interface ModelMapper {
    Pallet map(final PalletEntity palletEntity);
}
