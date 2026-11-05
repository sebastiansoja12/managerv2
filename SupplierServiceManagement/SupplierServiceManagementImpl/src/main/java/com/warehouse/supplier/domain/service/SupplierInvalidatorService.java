package com.warehouse.supplier.domain.service;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import org.springframework.stereotype.Service;

@Service
public class SupplierInvalidatorService {

    private final SupplierRepository supplierRepository;

    public SupplierInvalidatorService(final SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public void invalidateDriverLicense(final Supplier supplier) {
        supplier.markDriverLicenseAsInvalid();
        this.supplierRepository.update(supplier);
    }

    public void invalidateCertification(final Supplier supplier) {
        supplier.markCertificationAsInvalid();
        this.supplierRepository.update(supplier);
    }
}
