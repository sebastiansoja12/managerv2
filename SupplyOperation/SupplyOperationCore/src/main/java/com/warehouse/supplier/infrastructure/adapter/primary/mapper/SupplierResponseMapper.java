package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierAddResponse;
import com.warehouse.supplier.dto.SupplierAddResponseDto;
import com.warehouse.supplier.dto.SupplierDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SupplierResponseMapper {

    SupplierDto map(Supplier supplier);

    List<SupplierDto> mapToDto(List<Supplier> suppliers);

    @Mapping(source = "supplier.firstName", target = "firstName")
    @Mapping(source = "supplier.lastName", target = "lastName")
    @Mapping(source = "supplier.telephone", target = "telephone")
    @Mapping(source = "supplier.depotCode", target = "depotCode")
    @Mapping(source = "supplier.supplierCode", target = "supplierCode")
    @Mapping(source = "supplier.id", target = "id")
    SupplierAddResponseDto map(SupplierAddResponse response);

    List<SupplierAddResponseDto> map(List<SupplierAddResponse> response);
}
