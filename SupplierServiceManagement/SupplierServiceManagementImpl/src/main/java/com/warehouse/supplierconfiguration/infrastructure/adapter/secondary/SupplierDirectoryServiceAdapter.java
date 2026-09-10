package com.warehouse.supplierconfiguration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.dto.SupplierProfileDto;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierDirectoryServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierProfile;

import java.util.List;
import java.util.Optional;

public class SupplierDirectoryServiceAdapter implements SupplierDirectoryServicePort {

    private static final String ACTIVE_STATUS = "ACTIVE";

    private final SupplierApiService supplierApiService;

    public SupplierDirectoryServiceAdapter(final SupplierApiService supplierApiService) {
        this.supplierApiService = supplierApiService;
    }

    @Override
    public Optional<SupplierProfile> findBySupplierId(final SupplierId supplierId) {
        return this.supplierApiService.findProfileById(supplierId).map(this::map);
    }

    @Override
    public Optional<SupplierProfile> findBySupplierCode(final SupplierCode supplierCode) {
        return this.supplierApiService.findProfileByCode(supplierCode).map(this::map);
    }

    @Override
    public Optional<SupplierProfile> findByUserId(final UserId userId) {
        return this.supplierApiService.findProfileByUserId(userId).map(this::map);
    }

    @Override
    public List<SupplierProfile> findActiveSuppliers() {
        return this.supplierApiService.findProfiles()
                .stream()
                .map(this::map)
                .filter(SupplierProfile::active)
                .toList();
    }

    private SupplierProfile map(final SupplierProfileDto supplierProfile) {
        return new SupplierProfile(
                supplierProfile.supplierId(),
                supplierProfile.supplierCode(),
                supplierProfile.firstName(),
                supplierProfile.lastName(),
                supplierProfile.departmentId(),
                ACTIVE_STATUS.equals(supplierProfile.status()));
    }
}
