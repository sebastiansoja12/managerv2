package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.delivery.dto.DeviceIdDto;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedResponseDto;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponseDetails;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.dto.DeliveryRejectResponseDetailsDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;
import com.warehouse.deliveryreturn.domain.vo.ReturnToken;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDetailsDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.ReturnTokenDto;
import com.warehouse.logistics.domain.model.Response;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryRejectResponse;
import com.warehouse.terminal.model.DeliveryRejectResponseDetail;
import com.warehouse.terminal.model.DeliveryReturnResponseDetail;
import com.warehouse.terminal.response.DeviceUpToDate;
import com.warehouse.terminal.response.TerminalResponse;

public class LogisticsResponseMapper {

    public TerminalResponse map(final ProcessId processId, final Response response) {
        if (response == null) {
            return null;
        }

        final Device device = map(response.getDeviceInformation());
        final DeliveryRejectResponse deliveryRejectResponse = map(response.getDeliveryRejectResponse());
        final com.warehouse.terminal.model.DeliveryReturnResponse deliveryReturnResponse = map(response.getDeliveryReturnResponse());
        return new TerminalResponse(device, processId.getValue().toString(), deliveryRejectResponse, deliveryReturnResponse);
    }

    public String map(final SupplierCode supplierCode) {
        return supplierCode == null ? null : supplierCode.value();
    }

    public com.warehouse.terminal.model.DeliveryReturnResponse map(final DeliveryReturnResponse deliveryReturnResponse) {
        if (deliveryReturnResponse == null) {
            return null;
        }

        final List<DeliveryReturnResponseDetail> details = deliveryReturnResponse.getDeliveryReturnResponseDetails()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
        return new com.warehouse.terminal.model.DeliveryReturnResponse(details);
    }

    public DeliveryReturnResponseDetail map(final DeliveryReturnResponseDetails deliveryReturnResponseDetails) {
        final String supplierCode = deliveryReturnResponseDetails.getSupplierCode().value();
        final String departmentCode = deliveryReturnResponseDetails.getDepartmentCode().getValue();
        final Long shipmentId = deliveryReturnResponseDetails.getShipmentId().getValue();
        final String deliveryStatus = deliveryReturnResponseDetails.getDeliveryStatus().name();
        final String returnToken = deliveryReturnResponseDetails.getReturnToken().value();
        final String deliveryId = deliveryReturnResponseDetails.getDeliveryId() != null
                ? deliveryReturnResponseDetails.getDeliveryId().getId()
                : null;
        final String updateStatus = deliveryReturnResponseDetails.getUpdateStatus().name();
        return new DeliveryReturnResponseDetail(supplierCode, departmentCode, shipmentId, returnToken, updateStatus,
                deliveryStatus, deliveryId);
    }

    public DeliveryRejectResponse map(final com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse response) {
        if (response == null) {
            return null;
        }

        final List<DeliveryRejectResponseDetail> details = mapToRejectResponseDetail(response.getDeliveryRejectResponseDetails());
        return new DeliveryRejectResponse(details);
    }

    public Device map(final DeviceInformation deviceInformation) {
        final String version = deviceInformation.getVersion();
        final String username = deviceInformation.getUsername();
        final String deviceId = deviceInformation.getDeviceId().value();
        final String departmentCode = deviceInformation.getDepartmentCode().getValue();
        final String deviceUserType = deviceInformation.getDeviceUserType().toString();
        final String deviceType = deviceInformation.getDeviceType().toString();
        final DeviceUpToDate deviceUpToDate = deviceInformation.isUpdateRequired() ? DeviceUpToDate.NO : DeviceUpToDate.YES;
        return new Device(version, deviceId, username, departmentCode, deviceUserType, deviceType, deviceUpToDate);
    }

    public List<DeliveryRejectResponseDetail> mapToRejectResponseDetail(
            final List<DeliveryRejectResponseDetails> deliveryRejectResponseDetails) {
        if (deliveryRejectResponseDetails == null) {
            return new ArrayList<>();
        }

        return deliveryRejectResponseDetails.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public DeliveryRejectResponseDetail map(final DeliveryRejectResponseDetails deliveryRejectResponseDetails) {
        return new DeliveryRejectResponseDetail(
                deliveryRejectResponseDetails.getSupplierCode().value(),
                deliveryRejectResponseDetails.getDepartmentCode().value(),
                deliveryRejectResponseDetails.getShipmentId().getValue(),
                deliveryRejectResponseDetails.getNewShipmentId() != null
                        ? deliveryRejectResponseDetails.getNewShipmentId().getValue()
                        : null,
                deliveryRejectResponseDetails.getDeliveryStatus().name(),
                deliveryRejectResponseDetails.getRejectReason().getValue());
    }

    public DeviceInformation map(final DeviceInformationDto deviceInformationDto) {
        return DeviceInformation.builder()
                .deviceId(map(deviceInformationDto.deviceId()))
                .departmentCode(new DepartmentCode(deviceInformationDto.departmentCode().value()))
                .version(deviceInformationDto.version().value())
                .username(deviceInformationDto.username().value())
                .deviceUserType(com.warehouse.terminal.DeviceUserType.valueOf(deviceInformationDto.deviceUserType().name()))
                .deviceType(com.warehouse.commonassets.enumeration.DeviceType.valueOf(deviceInformationDto.deviceType().name()))
                .build();
    }

    public DeviceId map(final DeviceIdDto deviceId) {
        return new DeviceId(deviceId.value());
    }

    public List<DeliveryReturnResponse> mapToDeliveryReturnResponses(
            final List<DeliveryReturnResponseDto> deliveryReturnResponseDto) {
        if (deliveryReturnResponseDto == null) {
            return Collections.emptyList();
        }

        return deliveryReturnResponseDto.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public String map(final ReturnTokenDto value) {
        return value == null ? null : value.value();
    }

    public Response mapDeliveryRejectResponse(final DeliveryRejectResponseDto deliveryRejectResponse) {
        final DeviceInformation deviceInformation = map(deliveryRejectResponse.deviceInformation());
        final List<DeliveryRejectResponseDetails> details = mapToDeliveryRejectResponseDetailsList(
                deliveryRejectResponse.deliveryRejectResponseDetails());
        final com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse response =
                new com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse(details, deviceInformation);
        return new Response(deviceInformation, Collections.emptySet(), null, response, Collections.emptyList());
    }

    public List<DeliveryRejectResponseDetails> mapToDeliveryRejectResponseDetailsList(
            final List<DeliveryRejectResponseDetailsDto> deliveryRejectResponseDetailsDtos) {
        if (deliveryRejectResponseDetailsDtos == null) {
            return Collections.emptyList();
        }

        return deliveryRejectResponseDetailsDtos.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public DeliveryRejectResponseDetails map(final DeliveryRejectResponseDetailsDto dto) {
        return new DeliveryRejectResponseDetails(
                null,
                map(dto.getShipmentId()),
                map(dto.getNewShipmentId()),
                new SupplierCode(dto.getSupplierCode().value()),
                new DepartmentCode(dto.getDepartmentCode().value()),
                new RejectReason(dto.getRejectReason().value()),
                DeliveryStatus.valueOf(dto.getDeliveryStatus().name()),
                dto.getSuccess(),
                dto.getErrorMessage());
    }

    public ShipmentId map(final ShipmentIdDto shipmentId) {
        return shipmentId == null ? null : new ShipmentId(shipmentId.value());
    }

    public Response mapDeliveryMissedResponse(final DeliveryMissedResponseDto deliveryMissedResponse) {
        return null;
    }

    public Response mapDeliveryReturnResponse(final DeliveryReturnResponseDto deliveryReturnResponse) {
        final DeviceInformationDto deviceInformation = deliveryReturnResponse.getDeviceInformation();
        return new Response(map(deviceInformation), Collections.emptySet(), map(deliveryReturnResponse),
                null, Collections.emptyList());
    }

    public DeliveryReturnResponse map(final DeliveryReturnResponseDto deliveryReturnResponse) {
        return DeliveryReturnResponse
                .builder()
                .deliveryReturnResponseDetails(map(deliveryReturnResponse.getDeliveryReturnResponseDetails()))
                .deviceInformation(map(deliveryReturnResponse.getDeviceInformation()))
                .build();
    }

    public List<DeliveryReturnResponseDetails> map(final List<DeliveryReturnResponseDetailsDto> deliveryReturnResponseDetails) {
        return deliveryReturnResponseDetails.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public DeliveryReturnResponseDetails map(final DeliveryReturnResponseDetailsDto dto) {
        return new DeliveryReturnResponseDetails(
                dto.getProcessId() != null ? new ProcessId(dto.getProcessId().getProcessId()) : null,
                null,
                new DepartmentCode(dto.getDepartmentCode().value()),
                new SupplierCode(dto.getSupplierCode().value()),
                dto.getShipmentId() != null ? new ShipmentId(dto.getShipmentId().value()) : null,
                DeliveryStatus.valueOf(dto.getDeliveryStatus().name()),
                dto.getReturnToken() != null ? new ReturnToken(map(dto.getReturnToken())) : null,
                dto.getUpdateStatus() != null ? UpdateStatus.valueOf(dto.getUpdateStatus().name()) : null);
    }

}
