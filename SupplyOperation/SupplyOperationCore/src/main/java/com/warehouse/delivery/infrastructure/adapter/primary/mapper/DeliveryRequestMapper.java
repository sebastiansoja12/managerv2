package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.vo.RejectReason;
import com.warehouse.delivery.infrastructure.adapter.primary.dto.DeliveryRequestDto;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryRequestMapper {

    @Mapping(target = "deliveryStatus", ignore = true)
    DeliveryRequest map(DeliveryRequestDto deliveryRequest);
    List<DeliveryRequest> map(List<DeliveryRequestDto> deliveryRequest);
    Request map(final TerminalRequest terminalRequest);
    ShipmentId map(final Long value);
    RejectReason map(final String value);
}
