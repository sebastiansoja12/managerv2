package com.warehouse.supplier.api;

import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;

import java.util.List;

public interface SupplierApiService {
    List<SupplierDetailsDto> findAll();
    SupplierCodeDto create(final CreateSupplierApiRequest request);

    int anonymizeAll();
}
