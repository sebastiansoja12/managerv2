package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.infrastructure.adapter.primary.dto.DeliveryRequestDto;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryRequestMapper {

    @Mapping(target = "deliveryStatus", ignore = true)
    DeliveryRequest map(DeliveryRequestDto deliveryRequest);
    List<DeliveryRequest> map(List<DeliveryRequestDto> deliveryRequest);

    DeliveryRequest map(final TerminalRequest terminalRequest);
}
