package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.DeviceIdDto;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.DeviceTypeDto;
import com.warehouse.delivery.dto.DeviceUserTypeDto;
import com.warehouse.delivery.dto.ProcessTypeDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.delivery.dto.SupplierCodeDto;
import com.warehouse.delivery.dto.UsernameDto;
import com.warehouse.delivery.dto.VersionDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedDetailsDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.dto.DeliveryRejectDetailsDto;
import com.warehouse.deliveryreject.dto.RejectReasonDto;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnDetailsDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.logistics.domain.model.DeliveryMissedDetails;
import com.warehouse.logistics.domain.model.DeliveryMissedRequest;
import com.warehouse.logistics.domain.model.DeliveryRejectDetails;
import com.warehouse.logistics.domain.model.DeliveryRejectRequest;
import com.warehouse.logistics.domain.model.DeliveryReturnDetails;
import com.warehouse.logistics.domain.model.DeliveryReturnRequest;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.jaxb.DeliveryMissedDetailType;
import com.warehouse.terminal.jaxb.DeliveryMissedRequestType;
import com.warehouse.terminal.jaxb.DeliveryRejectDetailType;
import com.warehouse.terminal.jaxb.DeliveryRejectRequestType;
import com.warehouse.terminal.jaxb.DeliveryReturnDetailType;
import com.warehouse.terminal.jaxb.DeliveryReturnRequestType;
import com.warehouse.terminal.jaxb.DeliveryStatusEnum;
import com.warehouse.terminal.jaxb.DeviceType;
import com.warehouse.terminal.jaxb.ProcessTypeEnum;
import com.warehouse.terminal.jaxb.TerminalRequest;

public class LogisticsRequestMapper {

    public Request map(final TerminalRequest terminalRequest) {
        final ProcessType processType = map(terminalRequest.getProcessType());
        final DeviceInformation deviceInformation = map(terminalRequest.getDevice());
        final DeliveryRejectRequest deliveryRejectRequest = map(terminalRequest.getDeliveryRejectRequest());
        final DeliveryReturnRequest deliveryReturnRequest = map(terminalRequest.getDeliveryReturnRequest());
        final DeliveryMissedRequest deliveryMissedRequest = map(terminalRequest.getDeliveryMissedRequest());
        return new Request(processType, deviceInformation, deliveryRejectRequest, deliveryMissedRequest, deliveryReturnRequest);
    }

    public DeliveryRejectRequest map(final DeliveryRejectRequestType deliveryRejectRequest) {
        if (deliveryRejectRequest == null || deliveryRejectRequest.getDeliveryRejectDetails() == null) {
            return null;
        }
        final List<DeliveryRejectDetails> deliveryRejectDetails = deliveryRejectRequest.getDeliveryRejectDetails()
                .getDeliveryRejectDetail()
                .stream()
                .map(this::map)
                .toList();
        return new DeliveryRejectRequest(deliveryRejectDetails, null);
    }

    public DeliveryRejectDetails map(final DeliveryRejectDetailType detail) {
        return new DeliveryRejectDetails(
                new ShipmentId(detail.getShipmentID()),
                new DepartmentCode(detail.getDepartmentCode()),
                new SupplierCode(detail.getSupplierCode()),
                map(detail.getDeliveryStatus()),
                new RejectReason(detail.getRejectReason()));
    }

    public DeliveryMissedRequest map(final DeliveryMissedRequestType deliveryMissedRequest) {
        if (deliveryMissedRequest == null || deliveryMissedRequest.getDeliveryMissedDetails() == null) {
            return null;
        }
        final List<DeliveryMissedDetails> details = deliveryMissedRequest.getDeliveryMissedDetails()
                .getDeliveryMissedDetail()
                .stream()
                .map(this::map)
                .toList();
        return new DeliveryMissedRequest(details);
    }

    public DeliveryReturnRequest map(final DeliveryReturnRequestType deliveryReturnRequest) {
        if (deliveryReturnRequest == null || deliveryReturnRequest.getDeliveryReturnDetails() == null) {
            return null;
        }
        final List<DeliveryReturnDetails> details = deliveryReturnRequest.getDeliveryReturnDetails()
                .getDeliveryReturnDetail()
                .stream()
                .map(this::map)
                .toList();
        return new DeliveryReturnRequest(details);
    }

    public DeliveryReturnDetails map(final DeliveryReturnDetailType deliveryReturnDetail) {
        return new DeliveryReturnDetails(
                new ShipmentId(deliveryReturnDetail.getShipmentID()),
                new DepartmentCode(deliveryReturnDetail.getDepartmentCode()),
                new SupplierCode(deliveryReturnDetail.getSupplierCode()),
                map(deliveryReturnDetail.getDeliveryStatus()));
    }

    public DeliveryMissedDetails map(final DeliveryMissedDetailType deliveryMissedDetail) {
        return new DeliveryMissedDetails(
                new ShipmentId(deliveryMissedDetail.getShipmentID()),
                new DepartmentCode(deliveryMissedDetail.getDepartmentCode()),
                new SupplierCode(deliveryMissedDetail.getSupplierCode()),
                map(deliveryMissedDetail.getDeliveryStatus()));
    }

    public ProcessType map(final ProcessTypeEnum processType) {
        return ProcessType.valueOf(processType.name());
    }

    public DeliveryStatus map(final DeliveryStatusEnum deliveryStatus) {
        return DeliveryStatus.valueOf(deliveryStatus.name());
    }

    public DeviceInformation map(final DeviceType device) {
        return JaxbDeviceInformationMapper.map(device);
    }

    public DeliveryRejectRequestDto mapToDeliveryRejectRequest(final Request request) {
        return mapToDeliveryRejectRequest(request, null);
    }

    public DeliveryRejectRequestDto mapToDeliveryRejectRequest(final Request request,
                                                               final com.warehouse.commonassets.identificator.ProcessId processId) {
        final DeviceInformationDto deviceInformation = map(request.getDeviceInformation());
        final DeliveryRejectRequest deliveryRejectRequest = request.getDeliveryRejectRequest();
        final ProcessTypeDto processType = map(request.getProcessType());
        final List<DeliveryRejectDetailsDto> details = mapToDeliveryRejectDetails(deliveryRejectRequest.getDeliveryRejectDetails());
        return new DeliveryRejectRequestDto(details, deviceInformation, processType,
                processId == null ? null : processId.value());
    }

    public DeliveryMissedRequestDto mapToDeliveryMissedRequest(final Request request) {
        final List<DeliveryMissedDetailsDto> deliveryMissedDetails = request
                .getDeliveryMissedRequest()
                .getDeliveryMissedDetails()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
        return new DeliveryMissedRequestDto(null, deliveryMissedDetails, map(request.getDeviceInformation()));
    }

    public DeliveryReturnRequestDto mapToDeliveryReturnRequest(final Request request) {
        final List<DeliveryReturnDetailsDto> deliveryReturnDetails = request.getDeliveryReturnRequest() == null
                ? Collections.emptyList()
                : mapToDeliveryReturnDetails(request.getDeliveryReturnRequest().getDeliveryReturnDetails());
        final ProcessTypeDto processType = map(request.getProcessType());
        final DeviceInformationDto deviceInformation = map(request.getDeviceInformation());
        return new DeliveryReturnRequestDto(processType, deviceInformation, deliveryReturnDetails);
    }

    private List<DeliveryRejectDetailsDto> mapToDeliveryRejectDetails(final List<DeliveryRejectDetails> deliveryRejectDetails) {
        return deliveryRejectDetails.stream()
                .map(this::map)
                .toList();
    }

    private DeliveryRejectDetailsDto map(final DeliveryRejectDetails detail) {
        return new DeliveryRejectDetailsDto(
                map(detail.getShipmentId()),
                map(detail.getDepartmentCode()),
                new com.warehouse.deliveryreject.dto.SupplierCodeDto(detail.getSupplierCode().value()),
                map(detail.getDeliveryStatus()),
                new RejectReasonDto(detail.getRejectReason().getValue()));
    }

    private ProcessTypeDto map(final ProcessType processType) {
        return ProcessTypeDto.valueOf(processType.name());
    }

    private DeliveryMissedDetailsDto map(final DeliveryMissedDetails detail) {
        return new DeliveryMissedDetailsDto(
                map(detail.getShipmentId()),
                map(detail.getDepartmentCode()),
                map(detail.getSupplierCode()),
                map(detail.getDeliveryStatus()));
    }

    private List<DeliveryReturnDetailsDto> mapToDeliveryReturnDetails(final List<DeliveryReturnDetails> deliveryReturnDetails) {
        return deliveryReturnDetails.stream()
                .map(this::map)
                .toList();
    }

    private DeliveryReturnDetailsDto map(final DeliveryReturnDetails detail) {
        return new DeliveryReturnDetailsDto(
                map(detail.getShipmentId()),
                map(detail.getDeliveryStatus()),
                map(detail.getDepartmentCode()),
                map(detail.getSupplierCode()));
    }

    private DeviceInformationDto map(final DeviceInformation deviceInformation) {
        return new DeviceInformationDto(
                new DeviceIdDto(deviceInformation.getDeviceId().value()),
                new DepartmentCodeDto(deviceInformation.getDepartmentCode().value()),
                new VersionDto(deviceInformation.getVersion()),
                new UsernameDto(deviceInformation.getUsername()),
                DeviceUserTypeDto.valueOf(deviceInformation.getDeviceUserType().name()),
                DeviceTypeDto.valueOf(deviceInformation.getDeviceType().name()));
    }

    private ShipmentIdDto map(final ShipmentId shipmentId) {
        return new ShipmentIdDto(shipmentId.getValue());
    }

    private DepartmentCodeDto map(final DepartmentCode departmentCode) {
        return new DepartmentCodeDto(departmentCode.value());
    }

    private SupplierCodeDto map(final SupplierCode supplier) {
        return new SupplierCodeDto(supplier.value());
    }

    private DeliveryStatusDto map(final DeliveryStatus deliveryStatus) {
        return DeliveryStatusDto.valueOf(deliveryStatus.name());
    }
}
