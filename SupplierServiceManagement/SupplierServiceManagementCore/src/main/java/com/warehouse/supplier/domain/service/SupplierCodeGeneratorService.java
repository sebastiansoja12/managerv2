package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.identificator.SupplierCode;

public interface SupplierCodeGeneratorService {
    SupplierCode generate(final SupplierCode supplierCode);
}
