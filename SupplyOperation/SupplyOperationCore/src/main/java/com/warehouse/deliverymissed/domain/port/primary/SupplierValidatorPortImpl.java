package com.warehouse.deliverymissed.domain.port.primary;


import com.warehouse.deliverymissed.domain.port.secondary.SupplierRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierValidatorPortImpl implements SupplierValidatorPort {

    private final SupplierRepository supplierRepository;

    @Override
    public void validateSupplierCode(String supplierCode) {
        supplierRepository.validBySupplierCode(supplierCode);
    }
}
