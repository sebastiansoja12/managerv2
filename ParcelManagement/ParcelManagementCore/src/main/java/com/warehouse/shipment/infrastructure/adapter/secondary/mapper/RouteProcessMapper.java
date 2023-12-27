package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.routetracker.infrastructure.api.dto.RouteProcessDto;
import com.warehouse.shipment.domain.vo.RouteProcess;
import org.mapstruct.Mapper;

@Mapper
public interface RouteProcessMapper {
    RouteProcess map(RouteProcessDto routeProcess);
}
