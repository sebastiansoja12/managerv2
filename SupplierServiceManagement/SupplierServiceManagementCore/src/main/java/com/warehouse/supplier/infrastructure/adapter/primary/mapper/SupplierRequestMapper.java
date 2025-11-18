package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.supplier.domain.model.SupplierCreateRequest;
import org.mapstruct.Mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.dto.SupplierAddRequestDto;
import com.warehouse.supplier.dto.SupplierUpdateRequestDto;

@Mapper
public interface SupplierRequestMapper {
	List<SupplierCreateRequest> map(List<SupplierAddRequestDto> supplierAddRequestDtoList);

	SupplierCreateRequest map(SupplierAddRequestDto request);

	Supplier map(SupplierUpdateRequestDto updateRequest);
}
