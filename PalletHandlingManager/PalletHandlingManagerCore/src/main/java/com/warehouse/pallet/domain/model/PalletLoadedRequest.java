package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.PalletId;

public record PalletLoadedRequest(String eventId, PalletId palletId) {
}
