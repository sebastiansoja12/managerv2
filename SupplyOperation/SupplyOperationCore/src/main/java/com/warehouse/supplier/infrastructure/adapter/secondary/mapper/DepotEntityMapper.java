package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.DepotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DepotEntityMapper {

    @Mapping(source = "depot.city", target = "city")
    @Mapping(source = "depot.country", target = "country")
    @Mapping(source = "depot.depotCode", target = "depotCode")
    @Mapping(source = "depot.street", target = "street")
    DepotEntity map(Supplier supplier);
}
