package com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.enumeration.DeviceUserType;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.*;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedDetails;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.dto.DeliveryMissedDetailsDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;

@Mapper
public interface DeliveryMissedRequestMapper {

    default DeliveryMissedRequest map(final DeliveryMissedRequestDto deliveryMissedRequest) {
        final DeviceInformationDto deviceInformation = deliveryMissedRequest.getDeviceInformation();
        final Set<DeliveryMissedDetails> deliveryMissedDetails = deliveryMissedRequest.getDeliveryMissedDetails()
                .stream()
                .map(this::map)
                .collect(Collectors.toSet());
        return new DeliveryMissedRequest(deliveryMissedDetails, map(deviceInformation));
    }

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
