package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.department.domain.model.Depot;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepotEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DepotMapper {


    DepotEntity map(Depot depot);

    Depot map(DepotEntity depot);

    List<Depot> map(List<DepotEntity> depots);

    List<DepotEntity> mapToDepotEntityList(List<Depot> depots);

}
