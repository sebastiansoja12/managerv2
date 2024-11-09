package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.SupplierId;

public record Supplier(SupplierId supplierId, String firstName, String lastName, String supplierCode) {
}
