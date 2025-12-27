package com.warehouse.supplier.domain.port.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.vo.*;

public interface SupplyPort {
    SupplierCreateResponse create(final SupplierCreateCommand supplierCreateCommand);
    Supplier getOneById(final SupplierId supplierId);
    Supplier getOneByCode(final SupplierCode supplierCode);
    void addPackageType(final ChangeSupportedPackageTypeCommand request);
    void addDevice(final ChangeSupplierDeviceCommand request);
    Result<Void, String> update(final SupplierUpdateCommand request);
    DriverLicenseResponse updateDriverLicense(final DriverLicenseCommand request);
    CertificationUpdateResponse updateCertification(CertificationUpdateCommand request);
}
