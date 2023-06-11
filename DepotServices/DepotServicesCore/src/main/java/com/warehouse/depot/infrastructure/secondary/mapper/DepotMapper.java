package com.warehouse.depot.infrastructure.secondary.mapper;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.infrastructure.secondary.entity.DepotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DepotMapper {

    @Mapping(source = "coordinates.lon", target = "lon")
    @Mapping(source = "coordinates.lat", target = "lat")
    DepotEntity map(Depot depot);

    @Mapping(target = "coordinates.lon", source = "lon")
    @Mapping(target = "coordinates.lat", source = "lat")
    Depot map(DepotEntity depot);

    List<Depot> map(List<DepotEntity> depots);

    List<DepotEntity> mapToDepotEntityList(List<Depot> depots);

}
