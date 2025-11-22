package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import org.springframework.stereotype.Service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;

@Service
public class SupplierValidatorServiceImpl implements SupplierValidatorService {

    private final SupplierRepository supplierRepository;

    public SupplierValidatorServiceImpl(final SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public boolean validateDepartmentExists(final DepartmentCode departmentCode) {
        return false;
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
}
