package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.delivery.domain.model.DeliveryRejectRequest;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.infrastructure.adapter.primary.dto.DeliveryRequestDto;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryRequestMapper {

    @Mapping(target = "deliveryStatus", ignore = true)
    DeliveryRequest map(DeliveryRequestDto deliveryRequest);



    List<DeliveryRequest> map(List<DeliveryRequestDto> deliveryRequest);

    @Mapping(target = "shipmentId.value", source = "shipmentId")
    @Mapping(target = "rejectReason.value", source = "rejectReason")
    DeliveryRejectRequest map(com.warehouse.terminal.model.DeliveryRejectRequest deliveryRejectRequest);

    @Mapping(target = "deviceInformation", source = "device")
    Request map(final TerminalRequest terminalRequest);

    @Mapping(target = "deviceId.value", source = "deviceId")
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    DeviceInformation map(final Device device);
}
