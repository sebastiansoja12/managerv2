package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.DeviceIdDto;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedResponseDto;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponseDetails;
import com.warehouse.deliveryreject.dto.DeliveryRejectResponseDetailsDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDetailsDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.ReturnTokenDto;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryRejectResponse;
import com.warehouse.terminal.model.DeliveryRejectResponseDetail;
import com.warehouse.terminal.model.DeliveryReturnResponseDetail;
import com.warehouse.terminal.response.TerminalResponse;

@Mapper
public interface DeliveryResponseMapper {

    @Mapping(target = "device.username", source = "deviceInformation.username")
    @Mapping(target = "device.departmentCode", source = "deviceInformation.departmentCode.value")
    @Mapping(target = "device.deviceId", source = "deviceInformation.deviceId.value")
    TerminalResponse map(final Response response);

    String map(final SupplierCode supplierCode);

    com.warehouse.terminal.model.DeliveryReturnResponse map(final DeliveryReturnResponse deliveryReturnResponse);

    default DeliveryReturnResponseDetail map(final DeliveryReturnResponseDetails deliveryReturnResponseDetails) {
        final String supplierCode = deliveryReturnResponseDetails.getSupplierCode().getValue();
        final String departmentCode = deliveryReturnResponseDetails.getDepartmentCode().getValue();
        final Long shipmentId = deliveryReturnResponseDetails.getShipmentId().getValue();
        final String deliveryStatus = deliveryReturnResponseDetails.getDeliveryStatus().name();
        final String returnToken = deliveryReturnResponseDetails.getReturnToken().value();
        final String deliveryId = deliveryReturnResponseDetails.getDeliveryId() != null ? deliveryReturnResponseDetails.getDeliveryId().getId() : null;
        final String updateStatus = deliveryReturnResponseDetails.getUpdateStatus().name();
        return new DeliveryReturnResponseDetail(supplierCode, departmentCode, shipmentId, returnToken, updateStatus,
                deliveryStatus, deliveryId);
    }

    default DeliveryRejectResponse map(final com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse response) {
        final List<DeliveryRejectResponseDetail> deliveryRejectResponseDetails = new ArrayList<>();
        return new DeliveryRejectResponse(deliveryRejectResponseDetails);
    }

    default Device map(final DeviceInformation deviceInformation) {
        final String version = deviceInformation.getVersion();
        final String username = deviceInformation.getUsername();
        final Long deviceId = deviceInformation.getDeviceId().getValue();
        final String departmentCode = deviceInformation.getDepartmentCode().getValue();
        final String deviceUserType = deviceInformation.getDeviceUserType().toString();
        final String deviceType = deviceInformation.getDeviceType().toString();
        return new Device(version, deviceId, username, departmentCode, deviceUserType, deviceType);
    }

    List<DeliveryRejectResponseDetail> mapToRejectResponseDetail(final List<DeliveryRejectResponseDetails> deliveryRejectResponseDetails);

    @Mapping(target = "supplierCode", source = "supplierCode.value")
    @Mapping(target = "departmentCode", source = "departmentCode.value")
    @Mapping(target = "shipmentId", source = "shipmentId.value")
    @Mapping(target = "newShipmentId", source = "newShipmentId.value")
    @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    @Mapping(target = "rejectReason", source = "rejectReason.value")
    DeliveryRejectResponseDetail map(final DeliveryRejectResponseDetails deliveryRejectResponseDetails);

    @Mapping(target = "version", source = "version.value")
    @Mapping(target = "username", source = "username.value")
    DeviceInformation map(final DeviceInformationDto deviceInformationDto);

    default DeviceId map(final DeviceIdDto deviceId) {
        return new DeviceId(deviceId.value());
    }

    List<DeliveryReturnResponse> mapToDeliveryReturnResponses(final List<DeliveryReturnResponseDto> deliveryReturnResponseDto);

    String map(final ReturnTokenDto value);

    default Response mapDeliveryRejectResponse(final DeliveryRejectResponseDto deliveryRejectResponse) {
        final DeviceInformation deviceInformation = map(deliveryRejectResponse.deviceInformation());
        final List<DeliveryRejectResponseDetails> deliveryRejectResponseDetails = mapToDeliveryRejectResponseDetailsList(deliveryRejectResponse.deliveryRejectResponseDetails());
        final com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse response =
                new com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse(deliveryRejectResponseDetails, deviceInformation);
        return new Response(deviceInformation, Collections.emptySet(), null,
                response, Collections.emptyList());
    }

    List<DeliveryRejectResponseDetails> mapToDeliveryRejectResponseDetailsList(final List<DeliveryRejectResponseDetailsDto> deliveryRejectResponseDetailsDtos);

    DeliveryRejectResponseDetails map(final DeliveryRejectResponseDetailsDto deliveryRejectResponseDetailsDto);

    default Response mapDeliveryMissedResponse(final DeliveryMissedResponseDto deliveryMissedResponse) {
        return null;
    }

    default Response mapDeliveryReturnResponse(final DeliveryReturnResponseDto deliveryReturnResponse) {
        final DeviceInformationDto deviceInformation = deliveryReturnResponse.getDeviceInformation();
        return new Response(map(deviceInformation), Collections.emptySet(),
                map(deliveryReturnResponse), null, Collections.emptyList());
    }

    default DeliveryReturnResponse map(final DeliveryReturnResponseDto deliveryReturnResponse) {
        return DeliveryReturnResponse
                .builder()
                .deliveryReturnResponseDetails(map(deliveryReturnResponse.getDeliveryReturnResponseDetails()))
                .deviceInformation(map(deliveryReturnResponse.getDeviceInformation()))
                .build();
    }

    default List<DeliveryReturnResponseDetails> map(final List<DeliveryReturnResponseDetailsDto> deliveryReturnResponseDetails) {
        return deliveryReturnResponseDetails.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Mapping(target = "processId.value", source = "processId.processId")
    @Mapping(target = "shipmentId.value", source = "shipmentId.value")
    DeliveryReturnResponseDetails map(final DeliveryReturnResponseDetailsDto deliveryReturnResponseDetailsDto);

}
