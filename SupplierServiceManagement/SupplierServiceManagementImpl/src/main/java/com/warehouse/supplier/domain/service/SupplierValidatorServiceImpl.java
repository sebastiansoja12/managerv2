package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.port.secondary.DepartmentServicePort;
import com.warehouse.supplier.domain.port.secondary.DeviceServicePort;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.vo.SupplierUpdateCommand;
import org.springframework.stereotype.Service;

@Service
public class SupplierValidatorServiceImpl implements SupplierValidatorService {

    private final SupplierRepository supplierRepository;

    private final DeviceServicePort deviceServicePort;

    private final DepartmentServicePort departmentServicePort;

    public SupplierValidatorServiceImpl(final SupplierRepository supplierRepository,
                                        final DeviceServicePort deviceServicePort,
                                        final DepartmentServicePort departmentServicePort) {
        this.supplierRepository = supplierRepository;
        this.deviceServicePort = deviceServicePort;
        this.departmentServicePort = departmentServicePort;
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
    public boolean validateSupplierExistsById(final SupplierId supplierId) {
        return supplierRepository.findById(supplierId) != null;
    }

    @Override
    public Result<Void, String> validateSupplierForUpdate(final SupplierUpdateCommand supplierUpdateCommand) {
        final Result<Void, String> result;
        final DepartmentCode departmentCode = supplierUpdateCommand.departmentCode();
        final Result<Void, Void> departmentResult = departmentServicePort.validateDepartmentCode(departmentCode);
        if (departmentResult.isFailure()) {
            return Result.failure("Department with code " + departmentCode + " not found");
        }
        if (supplierRepository.findByCode(supplierUpdateCommand.supplierCode()) == null) {
            result = Result.failure("Supplier with code " + supplierUpdateCommand.supplierCode() + " not found");
        } else {
            result = Result.success();
        }
        return result;
    }

    @Override
    public Boolean validateDepartmentExistsByDeparmtneCode(final DepartmentCode departmentCode) {
        final Result<Void, Void> result = departmentServicePort.validateDepartmentCode(departmentCode);
        return result.isSuccess();
    }
}
