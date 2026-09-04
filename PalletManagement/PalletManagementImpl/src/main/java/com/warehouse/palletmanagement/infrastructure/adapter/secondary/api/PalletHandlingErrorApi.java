package com.warehouse.palletmanagement.infrastructure.adapter.secondary.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PalletHandlingErrorApi(String code, String message) {
}
