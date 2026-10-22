package com.warehouse.supplierconfiguration.application.port.primary.result;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;

import java.time.Instant;
import java.util.Map;

public record SupplierConfigurationResult(SupplierId supplierId,
                                          SupplierCode supplierCode,
                                          String firstName,
                                          String lastName,
                                          SupplierConfigurationValues values,
                                          SupplierCode substituteSupplierCode,
                                          Map<SupplierSetting, Boolean> enforcedSettings,
                                          Long version,
                                          Instant updatedAt,
                                          UserId updatedBy) {
}
