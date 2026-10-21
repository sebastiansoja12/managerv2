package com.warehouse.pallet.infrastructure.adapter.primary.dto;

import java.util.List;

public record PalletPageApi(List<PalletApi> content, int page, int size, long totalElements) {
}
