package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.model.Supplier;

import java.time.Instant;
import java.util.List;

public interface SupplierRepository {
    void create(final Supplier supplier);
    void update(final Supplier supplier);
    List<Supplier> findAllByCurrentDepartment();
    Supplier findById(final SupplierId supplierId);
    Supplier findByCode(final SupplierCode supplierCode);
    Supplier findByUserId(final UserId userId);
    List<Supplier> findAll();
    List<Supplier> findAllWithInvalidDriverLicense(final Instant now);
    List<Supplier> findAllWithExpiredDangerousGoodCertification(final Instant now);
}
