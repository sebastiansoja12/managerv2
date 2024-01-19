package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierAddRequest;
import com.warehouse.supplier.dto.SupplierAddRequestDto;
import com.warehouse.supplier.dto.SupplierUpdateRequestDto;

@Mapper
public interface SupplierRequestMapper {
	List<SupplierAddRequest> map(List<SupplierAddRequestDto> supplierAddRequestDtoList);

	SupplierAddRequest map(SupplierAddRequestDto request);

	Supplier map(SupplierUpdateRequestDto updateRequest);
}
