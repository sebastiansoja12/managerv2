package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.model.DeliveryArea;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.vo.DangerousGoodCertification;
import com.warehouse.supplier.domain.vo.DriverLicense;
import com.warehouse.supplier.domain.vo.SupplierDto;

public interface SupplierService {
    void create(final Supplier supplier);
    void activate(final SupplierId supplierId);
    void deactivate(final SupplierId supplierId);
    void updateDriverLicense(final SupplierCode supplierCode, final DriverLicense driverLicense);
    void updateUserCreated(final SupplierId supplierId, final UserId createdUserId);
    void updateDeliveryArea(final SupplierId supplierId, final DeliveryArea deliveryArea);
    void addSupportedPackageType(final SupplierCode supplierCode, final PackageType packageType);
    void addDevice(final SupplierCode supplierCode, final DeviceId deviceId);
    void update(final SupplierCode supplierCode, final SupplierDto supplier);

    Supplier findById(final SupplierId supplierId);
    Supplier findByCode(final SupplierCode supplierCode);
    SupplierId nextSupplierId();

    void invalidateDriverLicense(final SupplierCode supplierCode);

    void updateCertification(final SupplierCode supplierCode, final DangerousGoodCertification certification);
}
