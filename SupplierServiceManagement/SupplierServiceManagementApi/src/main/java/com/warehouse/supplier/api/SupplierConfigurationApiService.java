package com.warehouse.supplier.api;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.api.dto.SupplierConfigurationDto;

import java.util.Optional;

public interface SupplierConfigurationApiService {
    Optional<SupplierConfigurationDto> findBySupplierId(final SupplierId supplierId);
    Optional<SupplierConfigurationDto> findByUserId(final UserId userId);
}
