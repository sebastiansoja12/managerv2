package com.warehouse.supplier.domain.port.primary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.vo.DriverLicenseRequest;
import com.warehouse.supplier.domain.vo.DriverLicenseResponse;
import com.warehouse.supplier.domain.vo.SupplierCreateRequest;
import com.warehouse.supplier.domain.vo.SupplierCreateResponse;

public interface SupplyPort {
    SupplierCreateResponse create(final SupplierCreateRequest supplierCreateRequest);
    Supplier getOneById(final SupplierId supplierId);
    Supplier getOneByCode(final SupplierCode supplierCode);

    DriverLicenseResponse updateDriverLicense(final DriverLicenseRequest request);
}
