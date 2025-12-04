package com.warehouse.supplier.domain.port.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.event.SupplierCreated;
import com.warehouse.supplier.domain.exception.SupplierAlreadyExistsException;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.DeviceServicePort;
import com.warehouse.supplier.domain.registry.DomainContext;
import com.warehouse.supplier.domain.service.DriverLicenseService;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierService;
import com.warehouse.supplier.domain.service.SupplierValidatorService;
import com.warehouse.supplier.domain.vo.*;
import com.warehouse.supplier.infrastructure.adapter.secondary.exception.SupplierNotFoundException;

import java.time.Instant;

public class SupplyPortImpl implements SupplyPort {

    private final SupplierService supplierService;

    private final SupplierCodeGeneratorService generatorService;

    private final SupplierValidatorService validatorService;

    private final DriverLicenseService driverLicenseService;

    private final DeviceServicePort deviceServicePort;

    public SupplyPortImpl(final SupplierService supplierService,
                          final SupplierCodeGeneratorService generatorService,
                          final SupplierValidatorService validatorService,
                          final DriverLicenseService driverLicenseService,
                          final DeviceServicePort deviceServicePort) {
        this.supplierService = supplierService;
        this.generatorService = generatorService;
        this.validatorService = validatorService;
        this.driverLicenseService = driverLicenseService;
        this.deviceServicePort = deviceServicePort;
    }

    @Override
    public SupplierCreateResponse create(final SupplierCreateRequest supplierCreateRequest) {
        final SupplierCode supplierCode = this.generatorService.generate(supplierCreateRequest.supplierCode());
        validateNotExists(supplierCode);
        final String firstName = supplierCreateRequest.firstName();
        final String lastName = supplierCreateRequest.lastName();
        final String telephoneNumber = supplierCreateRequest.telephoneNumber();
        final SupplierId supplierId = this.supplierService.nextSupplierId();
		final Supplier supplier = new Supplier(supplierId, supplierCode, firstName, lastName, telephoneNumber);

        this.supplierService.create(supplier);

        DomainContext.publish(new SupplierCreated(supplier.snapshot(), Instant.now()));

        return new SupplierCreateResponse(supplier.supplierCode());
    }

    @Override
    public Supplier getOneById(final SupplierId supplierId) {
        return supplierService.findById(supplierId);
    }

    @Override
    public Supplier getOneByCode(final SupplierCode supplierCode) {
        return supplierService.findByCode(supplierCode);
    }

    @Override
    public void addPackageType(final ChangeSupportedPackageTypeRequest request) {
        this.supplierService.addSupportedPackageType(request.supplierCode(), request.packageType());
    }

    @Override
    public void addDevice(final ChangeSupplierDeviceRequest request) {
        final DeviceId deviceId = request.deviceId();
        final Result<Void, String> result = deviceServicePort.validateDevice(deviceId);
        if (result.isFailure()) {
            throw new IllegalArgumentException(result.getFailure());
        }
        final SupplierCode supplierCode = request.supplierCode();
        if (supplierService.findByCode(supplierCode) == null) {
            throw new SupplierNotFoundException(supplierCode.value());
        }
        this.supplierService.addDevice(supplierCode, deviceId);
    }

    @Override
    public Result<Void, String> update(final SupplierUpdateRequest request) {
        final SupplierCode supplierCode = request.supplierCode();
        final Result<Void, String> validationResult = this.validatorService.validateSupplierForUpdate(request);
        if (validationResult.isFailure()) {
            return Result.failure(validationResult.getFailure());
        }
        this.supplierService.update(supplierCode, SupplierDto.from(request));
        return Result.success();
    }

    @Override
    public DriverLicenseResponse updateDriverLicense(final DriverLicenseRequest request) {
        final DriverLicense driverLicense = request.driverLicense();
        final SupplierCode supplierCode = request.supplierCode();
        final Result<Void, String> validationResult = this.driverLicenseService.validateDriverLicense(driverLicense);
        if (validationResult.isSuccess()) {
            this.supplierService.updateDriverLicense(supplierCode, driverLicense);
            return DriverLicenseResponse.ok();
        } else {
            return DriverLicenseResponse.failure(validationResult.getFailure());
        }
    }

    private void validateNotExists(final SupplierCode supplierCode) {
        final Result<Void, String> result = this.validatorService.validateSupplierCode(supplierCode);
        if (result.isFailure()) {
            throw new SupplierAlreadyExistsException(result.getFailure());
        }
    }
}
