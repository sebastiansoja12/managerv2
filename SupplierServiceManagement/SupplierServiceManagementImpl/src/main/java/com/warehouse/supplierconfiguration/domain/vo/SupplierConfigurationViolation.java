package com.warehouse.supplierconfiguration.domain.vo;

import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationViolationCode;

public record SupplierConfigurationViolation(String field, SupplierConfigurationViolationCode code) {
}
