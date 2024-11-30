package com.warehouse.delivery.domain.port.primary;


import com.warehouse.delivery.domain.port.secondary.SupplierRepository;
import com.warehouse.delivery.infrastructure.adapter.primary.exception.RestException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplierValidatorPortImpl implements SupplierValidatorPort {

    private final SupplierRepository supplierRepository;

    @Override
    public void validateSupplierCode(final String supplierCode) {
        if (!supplierRepository.validBySupplierCode(supplierCode)) {
            throw new RestException(400, "Supplier is not valid");
        };
    }
}
