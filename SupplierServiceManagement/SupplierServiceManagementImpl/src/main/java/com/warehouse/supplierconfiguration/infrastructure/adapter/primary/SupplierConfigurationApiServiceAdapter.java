package com.warehouse.supplierconfiguration.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.api.SupplierConfigurationApiService;
import com.warehouse.supplier.api.dto.SupplierConfigurationDto;
import com.warehouse.supplierconfiguration.application.port.primary.SupplierConfigurationPort;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.mapper.SupplierConfigurationWebMapper;

import java.util.Optional;

public class SupplierConfigurationApiServiceAdapter implements SupplierConfigurationApiService {

    private final SupplierConfigurationPort supplierConfigurationPort;

    public SupplierConfigurationApiServiceAdapter(final SupplierConfigurationPort supplierConfigurationPort) {
        this.supplierConfigurationPort = supplierConfigurationPort;
    }

    @Override
    public Optional<SupplierConfigurationDto> findBySupplierId(final SupplierId supplierId) {
        return this.supplierConfigurationPort.findEffectiveBySupplierId(supplierId)
                .map(SupplierConfigurationWebMapper::mapToApiDto);
    }

    @Override
    public Optional<SupplierConfigurationDto> findByUserId(final UserId userId) {
        return this.supplierConfigurationPort.findEffectiveByUserId(userId)
                .map(SupplierConfigurationWebMapper::mapToApiDto);
    }
}
