package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteProcessDto;
import org.mapstruct.Mapper;

@Mapper
public interface RouteProcessMapper {
    RouteProcess map(RouteProcessDto routeProcess);
}
