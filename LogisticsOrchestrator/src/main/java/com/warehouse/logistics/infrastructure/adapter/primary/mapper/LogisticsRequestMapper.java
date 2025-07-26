package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.dto.*;
import com.warehouse.deliverymissed.dto.DeliveryMissedDetailsDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.dto.DeliveryRejectDetailsDto;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnDetailsDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.logistics.domain.model.*;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryMissedDetail;
import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface LogisticsRequestMapper {

    @Mapping(target = "deviceInformation", ignore = true)
    default DeliveryRejectRequest map(com.warehouse.terminal.model.DeliveryRejectRequest deliveryRejectRequest) {
        final List<DeliveryRejectDetails> deliveryRejectDetails = deliveryRejectRequest.getDeliveryRejectDetails()
                .stream()
                .map(detail -> new DeliveryRejectDetails(
                        new ShipmentId(detail.getShipmentId()),
                        new DepartmentCode(detail.getDepartmentCode()),
                        new SupplierCode(detail.getSupplierCode()),
                        DeliveryStatus.valueOf(detail.getDeliveryStatus()),
                        new RejectReason(detail.getRejectReason())))
                .toList();
        return new DeliveryRejectRequest(deliveryRejectDetails, null);
    }

    default Request map(final TerminalRequest terminalRequest) {
        final ProcessType processType = map(terminalRequest.getProcessType());
        final DeviceInformation deviceInformation = map(terminalRequest.getDevice());
        final DeliveryRejectRequest deliveryRejectRequest = map(terminalRequest.getDeliveryRejectRequest());
        final DeliveryReturnRequest deliveryReturnRequest = map(terminalRequest.getDeliveryReturnRequest());
        final DeliveryMissedRequest deliveryMissedRequest = map(terminalRequest.getDeliveryMissedRequest());
        return new Request(processType, deviceInformation, deliveryRejectRequest, deliveryMissedRequest, deliveryReturnRequest);
    }

    DeliveryMissedRequest map(final com.warehouse.terminal.model.DeliveryMissedRequest deliveryMissedRequest);

    DeliveryReturnRequest map(final com.warehouse.terminal.model.DeliveryReturnRequest deliveryReturnRequest);

    @Mapping(target = "shipmentId.value", source = "shipmentId")
    @Mapping(target = "supplierCode.value", source = "supplierCode")
    @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    default DeliveryReturnDetails map(final DeliveryReturnDetail deliveryReturnDetail) {
        return null;
    }

    @Mapping(target = "shipmentId.value", source = "shipmentId")
    @Mapping(target = "supplierCode.value", source = "supplierCode")
    @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    default DeliveryMissedDetails map(final DeliveryMissedDetail deliveryMissedDetail) {
        return null;
    }

    ProcessType map(final com.warehouse.terminal.enumeration.ProcessType processType);

    @Mapping(target = "departmentCode.value", source = "departmentCode")
    DeviceInformation map(final Device device);

    default DeviceId map(final Long deviceId) {
        return new DeviceId(deviceId);
    }

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
        final List<DeliveryMissedDetailsDto> deliveryMissedDetails = request
                .getDeliveryMissedRequest()
                .getDeliveryMissedDetails()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
        return new DeliveryMissedRequestDto(null, deliveryMissedDetails, map(request.getDeviceInformation()));
    }

    DeliveryMissedDetailsDto map(final DeliveryMissedDetails deliveryMissedDetails);

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
