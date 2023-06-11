package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.dto.SupplierDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SupplierResponseMapper {

    @Mapping(source = "depot.id", target = "depot.id", ignore = true)
    SupplierDto map(Supplier supplier);

    List<SupplierDto> map(List<Supplier> suppliers);
}
