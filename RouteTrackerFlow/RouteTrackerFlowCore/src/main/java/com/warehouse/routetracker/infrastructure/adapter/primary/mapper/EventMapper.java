package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.routetracker.domain.model.ShipmentRequest;
import com.warehouse.routetracker.domain.vo.DeliveryInformation;
import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.api.dto.DeliveryInformationDto;
import com.warehouse.routetracker.infrastructure.api.dto.RouteRequestDto;
import com.warehouse.routetracker.infrastructure.api.dto.RouteResponseDto;
import com.warehouse.routetracker.infrastructure.api.dto.ShipmentRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EventMapper {

    DeliveryInformation map(DeliveryInformationDto deliveryInformationDto);

    List<DeliveryInformation> map(List<DeliveryInformationDto> deliveryInformationDto);

    ShipmentRequest map(ShipmentRequestDto shipmentRequestDto);

    RouteRequest map(RouteRequestDto routeRequestDto);

    RouteResponse map(RouteResponseDto routeRequestDto);
}
