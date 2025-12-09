package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplier.domain.vo.SupplierSnapshot;
import com.warehouse.supplier.infrastructure.adapter.secondary.api.RegisterRequestDto;

public abstract class OutputRequestMapper {

    public static RegisterRequestDto map(final SupplierSnapshot snapshot) {
        return new RegisterRequestDto(
                null, snapshot.supplierCode().value(), null, snapshot.firstName(),
                snapshot.lastName(), "SUPPLIER", null
        );
    }
}
