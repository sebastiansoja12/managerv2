package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.event.SupplierUpdated;
import com.warehouse.supplier.domain.model.DeliveryArea;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.registry.DomainContext;
import com.warehouse.supplier.domain.vo.DriverLicense;
import com.warehouse.supplier.domain.vo.SupplierDto;

import java.time.Instant;
import java.util.UUID;


public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(final SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void create(final Supplier supplier) {
        this.supplierRepository.create(supplier);
    }

    @Override
    public void activate(final SupplierId supplierId) {
        final Supplier supplier = this.findById(supplierId);
        supplier.markAsActive();
        this.supplierRepository.update(supplier);
    }

    @Override
    public void deactivate(final SupplierId supplierId) {
        final Supplier supplier = this.findById(supplierId);
        supplier.markAsInactive();
        this.supplierRepository.update(supplier);
    }

    @Override
    public void updateDriverLicense(final SupplierCode supplierCode, final DriverLicense driverLicense) {
        final Supplier supplier = this.findByCode(supplierCode);
        supplier.changeDriverLicense(driverLicense);
        this.supplierRepository.update(supplier);
    }

    @Override
    public void updateUserCreated(final SupplierId supplierId, final UserId createdUserId) {
        final Supplier supplier = this.findById(supplierId);
        supplier.markUserCreated(createdUserId);
        this.supplierRepository.update(supplier);
    }

    @Override
    public void updateDeliveryArea(final SupplierId supplierId, final DeliveryArea deliveryArea) {
        final Supplier supplier = this.findById(supplierId);
        supplier.changeDeliveryArea(deliveryArea);
        this.supplierRepository.update(supplier);
    }

    @Override
    public void addSupportedPackageType(final SupplierCode supplierCode, final PackageType packageType) {
        final Supplier supplier = this.findByCode(supplierCode);
        supplier.addSupportedPackageType(packageType);
        this.supplierRepository.update(supplier);
    }

    @Override
    public void addDevice(final SupplierCode supplierCode, final DeviceId deviceId) {
        final Supplier supplier = this.findByCode(supplierCode);
        supplier.changeDeviceId(deviceId);
        this.supplierRepository.update(supplier);
    }

    @Override
    public void update(final SupplierCode supplierCode, final SupplierDto supp) {
        final Supplier supplier = this.findByCode(supplierCode);
        supplier.updateData(supp);
        this.supplierRepository.update(supplier);
        DomainContext.eventPublisher().publishEvent(
                new SupplierUpdated(supplier.snapshot(), Instant.now())
        );
    }

    @Override
    public Supplier findById(final SupplierId supplierId) {
        return supplierRepository.findById(supplierId);
    }

    @Override
    public Supplier findByCode(final SupplierCode supplierCode) {
        return supplierRepository.findByCode(supplierCode);
    }

    @Override
    public SupplierId nextSupplierId() {
        return new SupplierId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    @Override
    public void invalidateDriverLicense(final SupplierCode supplierCode) {
        final Supplier supplier = this.findByCode(supplierCode);
        supplier.markDriverLicenseAsInvalid();
        this.supplierRepository.update(supplier);
    }
}
