package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SupplierEntityMapper {

    SupplierEntity map(Supplier supplier);

    Supplier map(SupplierEntity supplier);

    List<Supplier> map(List<SupplierEntity> supplier);

    List<SupplierEntity> mapToListEntity(List<Supplier> suppliers);

}
