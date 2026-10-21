package com.warehouse.palletmanagement.domain.vo;

import java.util.List;

public record PalletPage(List<PalletDetails> pallets, int page, int size, long totalElements) {
}
