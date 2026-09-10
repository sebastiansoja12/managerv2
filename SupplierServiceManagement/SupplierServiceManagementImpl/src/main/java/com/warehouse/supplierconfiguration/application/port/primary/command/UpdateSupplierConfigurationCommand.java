package com.warehouse.supplierconfiguration.application.port.primary.command;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;

public record UpdateSupplierConfigurationCommand(SupplierConfigurationTarget target,
                                                 Long expectedVersion,
                                                 SupplierConfigurationValues values,
                                                 SupplierCode substituteSupplierCode) {
}
