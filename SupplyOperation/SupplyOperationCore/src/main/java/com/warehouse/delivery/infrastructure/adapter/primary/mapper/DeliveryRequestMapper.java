package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.model.DeliveryMissedRequest;
import com.warehouse.delivery.domain.model.DeliveryRejectRequest;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.*;
import com.warehouse.delivery.infrastructure.adapter.primary.dto.DeliveryRequestDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedInformationDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
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

    DeliveryRejectRequestDto map(final DeliveryRejectRequest deliveryRejectRequest);

    DeliveryMissedRequestDto map(final DeliveryMissedRequest deliveryMissedRequest);

    default List<DeliveryRejectRequestDto> mapToDeliveryRejectRequest(final Request request) {
        return request.getDeliveryRejectRequests().stream().map(this::map).collect(Collectors.toList());
    }

    default DeliveryMissedRequestDto mapToDeliveryMissedRequest(final Request request) {
        final List<DeliveryMissedInformationDto> deliveryMissedInformations = request.getDeliveryMissedRequests()
                .stream()
                .map(deliveryMissedRequest ->
                        new DeliveryMissedInformationDto(map(deliveryMissedRequest.getShipmentId()),
                        map(deliveryMissedRequest.getDepartmentCode()), map(deliveryMissedRequest.getSupplierCode()),
                                map(deliveryMissedRequest.getDeliveryStatus()))).toList();
        return new DeliveryMissedRequestDto(deliveryMissedInformations, map(request.getDeviceInformation()));
    }

    DeliveryStatusDto map(final DeliveryStatus deliveryStatus);

    DepartmentCodeDto map(final DepartmentCode departmentCode);

    SupplierCodeDto map(final SupplierCode supplier);

    ShipmentIdDto map(final ShipmentId shipmentId);

    @Mapping(target = "version.value", source = "version")
    @Mapping(target = "username.value", source = "version")
    DeviceInformationDto map(final DeviceInformation deviceInformation);

    default DeliveryReturnRequestDto mapToDeliveryReturnRequest(final Request request) {
        return new DeliveryReturnRequestDto();
    }
}
