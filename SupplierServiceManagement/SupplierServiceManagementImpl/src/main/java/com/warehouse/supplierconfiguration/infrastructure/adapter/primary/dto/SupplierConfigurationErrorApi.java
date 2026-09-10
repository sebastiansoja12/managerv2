package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto;

import java.util.List;

public record SupplierConfigurationErrorApi(String code, String message, List<SupplierConfigurationFieldErrorApi> fieldErrors) {

    public record SupplierConfigurationFieldErrorApi(String field, String code) {
    }
}
