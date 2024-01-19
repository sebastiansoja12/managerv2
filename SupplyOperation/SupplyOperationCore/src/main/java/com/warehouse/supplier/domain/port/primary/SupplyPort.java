package com.warehouse.supplier.domain.port.primary;

import java.util.List;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierAddRequest;
import com.warehouse.supplier.domain.vo.SupplierAddResponse;

public interface SupplyPort {
    List<Supplier> findAllSuppliers();

    List<SupplierAddResponse> createMultipleSuppliers(List<SupplierAddRequest> suppliers);

    Supplier updateSupplier(Supplier supplierUpdate);

    List<Supplier> findSuppliersByDepotCode(String depotCode);

    Supplier findSupplierByCode(String supplierCode);

}
