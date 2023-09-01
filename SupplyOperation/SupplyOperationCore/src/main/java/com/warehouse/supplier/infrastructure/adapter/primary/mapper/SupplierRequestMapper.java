package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.supplier.domain.model.SupplierAddRequest;
import com.warehouse.supplier.dto.SupplierAddRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SupplierRequestMapper {
    List<SupplierAddRequest> map(List<SupplierAddRequestDto> supplierAddRequestDtoList);
    SupplierAddRequest map(SupplierAddRequestDto request);
}
