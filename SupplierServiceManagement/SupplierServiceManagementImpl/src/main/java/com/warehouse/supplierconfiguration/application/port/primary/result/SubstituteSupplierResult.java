package com.warehouse.supplierconfiguration.application.port.primary.result;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;

public record SubstituteSupplierResult(SupplierId supplierId,
                                       SupplierCode supplierCode,
                                       String firstName,
                                       String lastName) {
}
