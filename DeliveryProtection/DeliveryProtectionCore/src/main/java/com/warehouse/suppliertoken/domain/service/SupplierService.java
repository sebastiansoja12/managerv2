package com.warehouse.suppliertoken.domain.service;

import com.warehouse.suppliertoken.domain.model.Parcel;
import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;

import java.util.List;

public interface SupplierService {
    SupplierTokenResponse protect(SupplierTokenRequest request, List<Parcel> parcels);
}
