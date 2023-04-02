package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.dto.SupplierAddRequest;
import com.warehouse.supplier.dto.SupplierDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SupplierRequestMapper {
    Supplier map(SupplierDto supplierDto);

    Supplier map(SupplierAddRequest supplierDto);

    List<Supplier> map(List<SupplierAddRequest> supplierAddRequestList);
}
