package com.warehouse.palletmanagement.infrastructure.adapter.secondary.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PalletHandlingPageApi(List<PalletHandlingPalletApi> content, int page, int size, long totalElements) {
}
