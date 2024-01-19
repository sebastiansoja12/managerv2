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

    @Mapping(target = "depot.depotCode", source = "depotCode")
    SupplierEntity map(Supplier supplier);

    @Mapping(source = "depot.depotCode", target = "depotCode")
    SupplierModelRequest mapToModel(SupplierEntity supplier);

    @Mapping(source = "depot.depotCode", target = "depotCode")
    Supplier map(SupplierEntity supplier);

    List<Supplier> map(List<SupplierEntity> supplier);

    List<SupplierEntity> mapToListEntity(List<Supplier> suppliers);
}
