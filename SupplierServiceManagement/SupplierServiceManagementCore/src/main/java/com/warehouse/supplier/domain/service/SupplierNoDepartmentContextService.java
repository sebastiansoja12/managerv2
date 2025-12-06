package com.warehouse.supplier.domain.service;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.SupplierNoDepartmentContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierNoDepartmentContextService {

    private final SupplierNoDepartmentContextRepository supplierNoDepartmentContextRepository;

    public SupplierNoDepartmentContextService(final SupplierNoDepartmentContextRepository supplierNoDepartmentContextRepository) {
        this.supplierNoDepartmentContextRepository = supplierNoDepartmentContextRepository;
    }

    public List<Supplier> findAll() {
        return this.supplierNoDepartmentContextRepository.findAll();
    }

    public void invalidateDriverLicense(final Supplier supplier) {
        supplier.markDriverLicenseAsInvalid();
        this.supplierNoDepartmentContextRepository.update(supplier);
    }
}
