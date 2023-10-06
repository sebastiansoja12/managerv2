package com.warehouse.suppliertoken.domain.service;

import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;

public interface SupplierService {
    SupplierTokenResponse protect(SupplierTokenRequest request);
}
