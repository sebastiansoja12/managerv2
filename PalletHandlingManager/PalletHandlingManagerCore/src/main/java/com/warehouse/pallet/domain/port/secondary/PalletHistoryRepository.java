package com.warehouse.pallet.domain.port.secondary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.PalletHistoryEntry;

import java.util.List;

public interface PalletHistoryRepository {
    void append(final PalletHistoryEntry entry);
    List<PalletHistoryEntry> findByPalletId(final PalletId palletId);
}
