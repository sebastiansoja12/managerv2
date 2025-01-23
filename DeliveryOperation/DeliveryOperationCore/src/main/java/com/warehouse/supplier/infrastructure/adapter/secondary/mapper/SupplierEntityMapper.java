package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierModelRequest;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SupplierEntityMapper {

    @Mapping(target = "department.departmentCode", source = "departmentCode")
    SupplierEntity map(Supplier supplier);

    @Mapping(source = "department.departmentCode", target = "departmentCode")
    SupplierModelRequest mapToModel(SupplierEntity supplier);

    @Mapping(source = "department.departmentCode", target = "departmentCode")
    Supplier map(SupplierEntity supplier);

    List<Supplier> map(List<SupplierEntity> supplier);

    List<SupplierEntity> mapToListEntity(List<Supplier> suppliers);
}
