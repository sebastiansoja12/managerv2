package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.identificator.SupplierCode;

public record SupplierBasicDataUpdateCommand(SupplierCode supplierCode,
                                             String firstName,
                                             String lastName,
                                             String telephoneNumber) {
}
