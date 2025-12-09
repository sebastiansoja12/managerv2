package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.vo.SupplierUpdateRequest;

public interface SupplierValidatorService {
    Result<Void, String> validateSupplierCode(final SupplierCode supplierCode);
    boolean validateFirstName(final String firstName);
    boolean validateLastName(final String lastName);
    boolean validateTelephoneNumber(final String telephoneNumber);
    boolean validateEmail(final String email);
    boolean validateAddress(final String address);
    boolean validateCity(final String city);
    boolean validatePostalCode(final String postalCode);
    boolean validateCountry(final String country);
    boolean validateSupplierId(final SupplierId supplierId);
    Result<Void, String> validateSupplierForUpdate(final SupplierUpdateRequest supplierUpdateRequest);
}
