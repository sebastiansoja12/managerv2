package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletType;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.domain.vo.SealNumber;

public record PalletUpdate(PalletType palletType,
                           PalletHandlingPriority palletHandlingPriority,
                           Boolean refrigerated,
                           Dimension dimension,
                           SealNumber sealNumber) {
}
