package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.Collections;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedResponseDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.ReturnTokenDto;
import com.warehouse.terminal.model.DeliveryRejectResponse;
import com.warehouse.terminal.response.TerminalResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryResponseMapper {

    @Mapping(target = "device.username", source = "deviceInformation.username")
    @Mapping(target = "device.departmentCode", source = "deviceInformation.departmentCode.value")
    @Mapping(target = "device.deviceId", source = "deviceInformation.deviceId.value")
    TerminalResponse map(final Response response);

    String map(final SupplierCode supplierCode);

    @Mapping(target = "supplierCode", source = "supplierCode.value")
    @Mapping(target = "shipmentId", source = "shipmentId.value")
    @Mapping(target = "newShipmentId", source = "newShipmentId.value")
    @Mapping(target = "rejectReason", source = "rejectReason.value")
    DeliveryRejectResponse map(final com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse response);

    @Mapping(target = "version", source = "version.value")
    @Mapping(target = "username", source = "username.value")
    DeviceInformation map(final DeviceInformationDto deviceInformationDto);

    List<com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse> map(final List<DeliveryRejectResponseDto> deliveryRejectResponse);

    List<DeliveryReturnResponse> mapToDeliveryReturnResponses(final List<DeliveryReturnResponseDto> deliveryReturnResponseDto);

    String map(final ReturnTokenDto value);

    default Response mapDeliveryRejectResponse(final List<DeliveryRejectResponseDto> deliveryRejectResponse) {
        final DeviceInformationDto deviceInformation = deliveryRejectResponse.stream()
                .map(DeliveryRejectResponseDto::deviceInformation)
                .findFirst()
                .orElse(null);
        return new Response(Collections.emptySet(), map(deviceInformation), Collections.emptyList(),
                map(deliveryRejectResponse),Collections.emptyList());
    }

    default Response mapDeliveryMissedResponse(final DeliveryMissedResponseDto deliveryMissedResponse) {
        return null;
    }

    default Response mapDeliveryReturnResponse(final DeliveryReturnResponseDto deliveryReturnResponse) {
        final DeviceInformationDto deviceInformation = deliveryReturnResponse.getDeviceInformation();
        return new Response(Collections.emptySet(), map(deviceInformation),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }
}
