package com.warehouse.deliveryreturn.domain.port.primary;

import com.warehouse.deliveryreturn.domain.port.secondary.SupplierCodeValidatorServicePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierCodeValidatorPortImpl implements SupplierCodeValidatorPort {

    private final SupplierCodeValidatorServicePort supplierCodeValidatorServicePort;

    @Override
    public void validateSupplierCode(String supplierCode) {
        supplierCodeValidatorServicePort.validateSupplier(supplierCode);
    }
}
