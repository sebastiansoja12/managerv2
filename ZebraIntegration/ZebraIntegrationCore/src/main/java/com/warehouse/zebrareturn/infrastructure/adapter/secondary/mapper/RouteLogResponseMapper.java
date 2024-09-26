package com.warehouse.zebrareturn.infrastructure.adapter.secondary.mapper;


import com.warehouse.commonassets.vo.RouteProcess;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.api.RouteProcessDto;
import org.mapstruct.Mapper;


@Mapper
public interface RouteLogResponseMapper {

    RouteProcess map(final RouteProcessDto routeProcessResponseEntity);
}
