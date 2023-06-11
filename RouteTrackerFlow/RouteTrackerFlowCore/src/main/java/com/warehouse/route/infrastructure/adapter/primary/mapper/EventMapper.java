package com.warehouse.route.infrastructure.adapter.primary.mapper;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.ShipmentRequest;
import com.warehouse.route.domain.vo.SupplyInformation;
import com.warehouse.route.infrastructure.api.dto.RouteRequestDto;
import com.warehouse.route.infrastructure.api.dto.RouteResponseDto;
import com.warehouse.route.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.route.infrastructure.api.dto.SupplyInformationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EventMapper {

    SupplyInformation map(SupplyInformationDto supplyInformationDto);

    ShipmentRequest map(ShipmentRequestDto shipmentRequestDto);

    RouteRequest map(RouteRequestDto routeRequestDto);

    RouteResponse map(RouteResponseDto routeRequestDto);

    List<RouteRequest> mapToMultipleRouteRequests(List<RouteRequestDto> routeRequest);
}
