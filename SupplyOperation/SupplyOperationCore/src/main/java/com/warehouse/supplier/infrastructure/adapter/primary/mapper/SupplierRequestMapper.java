package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.dto.SupplierAddRequest;
import com.warehouse.supplier.dto.SupplierDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SupplierRequestMapper {
    Supplier map(SupplierDto supplierDto);

    Supplier map(SupplierAddRequest supplierDto);

    List<Supplier> map(List<SupplierAddRequest> supplierAddRequestList);
}
