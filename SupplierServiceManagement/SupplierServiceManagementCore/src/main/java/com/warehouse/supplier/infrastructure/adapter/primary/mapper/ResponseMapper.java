package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.supplier.domain.vo.SupplierCreateResponse;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.SupplierCreateApiResponse;

public abstract class ResponseMapper {

    public static SupplierCreateApiResponse map(final SupplierCreateResponse response) {
        return new SupplierCreateApiResponse(response.supplierCode().value());
    }
}
