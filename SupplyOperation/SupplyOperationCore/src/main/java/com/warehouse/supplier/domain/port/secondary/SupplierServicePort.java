package com.warehouse.supplier.domain.port.secondary;

import java.util.List;

import com.warehouse.supplier.domain.model.SupplierModelRequest;
import com.warehouse.supplier.domain.vo.SupplierModelResponse;

public interface SupplierServicePort {
    List<SupplierModelResponse> createSuppliers(List<SupplierModelRequest> suppliers);
}
