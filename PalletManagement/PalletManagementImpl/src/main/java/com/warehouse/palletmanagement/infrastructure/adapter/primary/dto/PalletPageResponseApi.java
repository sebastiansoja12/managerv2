package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

import java.util.List;

public record PalletPageResponseApi(List<PalletResponseApi> content, int page, int size, long totalElements) {
}
