package com.warehouse.pallet.domain.model;

import java.util.List;

public record PalletPage(List<Pallet> pallets, int page, int size, long totalElements) {
}
