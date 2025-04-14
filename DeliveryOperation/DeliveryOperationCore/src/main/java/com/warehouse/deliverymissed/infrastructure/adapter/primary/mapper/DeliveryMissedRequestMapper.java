package com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.delivery.dto.*;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedDetails;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.dto.DeliveryMissedDetailsDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.DeviceUserType;

@Mapper
public interface DeliveryMissedRequestMapper {

    default DeliveryMissedRequest map(final DeliveryMissedRequestDto deliveryMissedRequest) {
        final DeviceInformationDto deviceInformation = deliveryMissedRequest.getDeviceInformation();
        final Set<DeliveryMissedDetails> deliveryMissedDetails = deliveryMissedRequest.getDeliveryMissedDetails()
                .stream()
                .map(this::map)
                .collect(Collectors.toSet());
        final DeliveryId deliveryId = map(deliveryMissedRequest.getDeliveryId());
        return new DeliveryMissedRequest(deliveryId, deliveryMissedDetails, map(deviceInformation));
    }

    DeliveryId map(final DeliveryIdDto deliveryId);

    DeliveryMissedDetails map(final DeliveryMissedDetailsDto deliveryMissedDetailsDto);

    default DeviceInformation map(final DeviceInformationDto deviceInformation) {
        return DeviceInformation.builder()
                .deviceId(map(deviceInformation.deviceId()))
                .deviceType(map(deviceInformation.deviceType()))
                .username(deviceInformation.username().value())
                .version(deviceInformation.version().value())
                .deviceUserType(map(deviceInformation.deviceUserType()))
                .departmentCode(map(deviceInformation.departmentCode()))
                .build();
    }

    DeviceUserType map(final DeviceUserTypeDto deviceUserTypeDto);

    DeviceType map(final DeviceTypeDto deviceTypeDto);

    DeviceId map(final DeviceIdDto deviceId);

    DepartmentCode map(final DepartmentCodeDto departmentCode);

    SupplierCode map(final SupplierCodeDto supplierCode);

    ShipmentId map(final ShipmentIdDto shipmentId);

}
