package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.Collections;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.model.*;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.*;
import com.warehouse.deliverymissed.dto.DeliveryMissedInformationDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;
import com.warehouse.deliveryreject.dto.DeliveryRejectDetailsDto;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnDetailsDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryRejectDetail;
import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryRequestMapper {

    @Mapping(target = "deviceInformation", ignore = true)
    DeliveryRejectRequest map(com.warehouse.terminal.model.DeliveryRejectRequest deliveryRejectRequest);

    @Mapping(target = "shipmentId.value", source = "shipmentId")
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    @Mapping(target = "supplierCode.value", source = "supplierCode")
    @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    @Mapping(target = "rejectReason.value", source = "rejectReason")
    DeliveryRejectDetails map(final DeliveryRejectDetail deliveryRejectDetail);

    default Request map(final TerminalRequest terminalRequest) {
        final ProcessType processType = map(terminalRequest.getProcessType());
        final DeviceInformation deviceInformation = map(terminalRequest.getDevice());
        final DeliveryRejectRequest deliveryRejectRequest = map(terminalRequest.getDeliveryRejectRequest());
        final DeliveryReturnRequest deliveryReturnRequest = map(terminalRequest.getDeliveryReturnRequest());
        return new Request(processType, deviceInformation, deliveryRejectRequest, Collections.emptyList(), deliveryReturnRequest);
    }

    DeliveryReturnRequest map(final com.warehouse.terminal.model.DeliveryReturnRequest deliveryReturnRequest);

    @Mapping(target = "shipmentId.value", source = "shipmentId")
    @Mapping(target = "supplierCode.value", source = "supplierCode")
    @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    DeliveryReturnDetails map(final DeliveryReturnDetail deliveryReturnDetail);

    ProcessType map(final com.warehouse.terminal.enumeration.ProcessType processType);

    @Mapping(target = "deviceId.value", source = "deviceId")
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    DeviceInformation map(final Device device);

    DeliveryRejectRequestDto map(final DeliveryRejectRequest deliveryRejectRequest);

    DeliveryMissedRequestDto map(final DeliveryMissedRequest deliveryMissedRequest);

    default DeliveryRejectRequestDto mapToDeliveryRejectRequest(final Request request) {
        final DeviceInformationDto deviceInformation = map(request.getDeviceInformation());
        final DeliveryRejectRequest deliveryRejectRequest = request.getDeliveryRejectRequest();
        final ProcessTypeDto processType = map(request.getProcessType());
        return new DeliveryRejectRequestDto(mapToDeliveryRejectDetails(deliveryRejectRequest.getDeliveryRejectDetails()),
                deviceInformation, processType);
    }

    List<DeliveryRejectDetailsDto> mapToDeliveryRejectDetails(final List<DeliveryRejectDetails> deliveryRejectDetails);

    ProcessTypeDto map(final ProcessType processType);

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
    @Mapping(target = "username.value", source = "username")
    DeviceInformationDto map(final DeviceInformation deviceInformation);

    default DeliveryReturnRequestDto mapToDeliveryReturnRequest(final Request request) {
        final List<DeliveryReturnDetailsDto> deliveryReturnDetails =
                mapToDeliveryReturnDetails(request.getDeliveryReturnRequest().getDeliveryReturnDetails());
        final ProcessTypeDto processType = map(request.getProcessType());
        final DeviceInformationDto deviceInformation = map(request.getDeviceInformation());
        return new DeliveryReturnRequestDto(processType, deviceInformation, deliveryReturnDetails);
    }

    List<DeliveryReturnDetailsDto> mapToDeliveryReturnDetails(final List<DeliveryReturnDetails> deliveryReturnDetails);
}
