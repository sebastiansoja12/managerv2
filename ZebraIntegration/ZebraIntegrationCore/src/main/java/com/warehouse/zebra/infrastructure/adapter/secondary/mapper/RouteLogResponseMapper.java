package com.warehouse.zebra.infrastructure.adapter.secondary.mapper;


import org.mapstruct.Mapper;

import com.warehouse.zebra.domain.vo.RouteProcess;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.RouteProcessDto;

@Mapper
public interface RouteLogResponseMapper {

    RouteProcess map(RouteProcessDto routeProcessResponseEntity);
}
