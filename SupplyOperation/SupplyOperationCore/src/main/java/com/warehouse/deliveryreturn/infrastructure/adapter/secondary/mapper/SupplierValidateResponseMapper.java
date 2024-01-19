package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreturn.domain.vo.Supplier;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.SupplierDto;
import org.mapstruct.Mapper;

@Mapper
public interface SupplierValidateResponseMapper {
    Supplier map(SupplierDto supplier);
}
