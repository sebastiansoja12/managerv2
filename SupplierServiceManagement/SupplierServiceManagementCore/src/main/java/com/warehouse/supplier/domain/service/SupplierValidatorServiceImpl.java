package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.port.secondary.DeviceServicePort;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.vo.SupplierUpdateCommand;
import org.springframework.stereotype.Service;

@Service
public class SupplierValidatorServiceImpl implements SupplierValidatorService {

    private final SupplierRepository supplierRepository;

    private final DeviceServicePort deviceServicePort;

    public SupplierValidatorServiceImpl(final SupplierRepository supplierRepository,
                                        final DeviceServicePort deviceServicePort) {
        this.supplierRepository = supplierRepository;
        this.deviceServicePort = deviceServicePort;
    }

    @Override
    public Result<Void, String> validateSupplierCode(final SupplierCode supplierCode) {
        if (supplierRepository.findByCode(supplierCode) != null) {
            return Result.failure("Supplier with code " + supplierCode + " already exists");
        }
        return Result.success();
    }

    @Override
    public boolean validateFirstName(final String firstName) {
        return false;
    }

    @Override
    public boolean validateLastName(final String lastName) {
        return false;
    }

    @Override
    public boolean validateTelephoneNumber(final String telephoneNumber) {
        return false;
    }

    @Override
    public boolean validateEmail(final String email) {
        return false;
    }

    @Override
    public boolean validateAddress(final String address) {
        return false;
    }

    @Override
    public boolean validateCity(final String city) {
        return false;
    }

    @Override
    public boolean validatePostalCode(final String postalCode) {
        return false;
    }

    @Override
    public boolean validateCountry(final String country) {
        return false;
    }

    @Override
    public boolean validateSupplierId(final SupplierId supplierId) {
        return false;
    }

    @Override
    public Result<Void, String> validateSupplierForUpdate(final SupplierUpdateCommand supplierUpdateCommand) {
        final Result<Void, String> result;
        final Result<Void, String> deviceValidationResult = deviceServicePort.validateDevice(supplierUpdateCommand.deviceId());
        if (supplierRepository.findByCode(supplierUpdateCommand.supplierCode()) == null) {
            result = Result.failure("Supplier with code " + supplierUpdateCommand.supplierCode() + " not found");
        } else if (deviceValidationResult.isFailure()) {
            result = deviceValidationResult;
        } else {
            result = Result.success();
        }
        return result;
    }
}
