package com.warehouse.supplier.domain.port.primary;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierCreateRequest;
import com.warehouse.supplier.domain.vo.SupplierAddResponse;

import java.util.List;

public interface SupplyPort {
    List<Supplier> findAllSuppliers();

    List<SupplierAddResponse> createMultipleSuppliers(List<SupplierCreateRequest> suppliers);

    Supplier updateSupplier(Supplier supplierUpdate);

    List<Supplier> findSuppliersByDepotCode(String depotCode);

    Supplier findSupplierByCode(String supplierCode);

}
