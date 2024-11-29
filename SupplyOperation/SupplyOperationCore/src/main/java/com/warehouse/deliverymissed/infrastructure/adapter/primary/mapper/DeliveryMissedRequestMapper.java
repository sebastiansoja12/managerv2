package com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.enumeration.DeviceUserType;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.*;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedInformation;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedRequest;
import com.warehouse.deliverymissed.dto.DeliveryMissedInformationDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;

@Mapper
public interface DeliveryMissedRequestMapper {

    default DeliveryMissedRequest map(final DeliveryMissedRequestDto deliveryMissedRequest) {
        final DeviceInformationDto deviceInformation = deliveryMissedRequest.getDeviceInformationDto();
        final List<DeliveryMissedInformation> deliveryMissedInformations = deliveryMissedRequest
                .getDeliveryMissedInformations()
                .stream()
                .map(this::map)
                .toList();
        return new DeliveryMissedRequest(null, deliveryMissedInformations,
                map(deviceInformation));
    }

    DeliveryMissedInformation map(final DeliveryMissedInformationDto deliveryMissedInformationDto);

    default DeviceInformation map(final DeviceInformationDto deviceInformation) {
        return DeviceInformation.builder()
                .deviceId(map(deviceInformation.deviceId()))
                .deviceType(DeviceType.TERMINAL)
                .username(deviceInformation.username().value())
                .version(deviceInformation.version().value())
                .deviceUserType(DeviceUserType.SUPPLIER)
                .departmentCode(map(deviceInformation.departmentCode()))
                .build();
    }

    DeviceId map(final DeviceIdDto deviceId);

    DepartmentCode map(final DepartmentCodeDto departmentCode);

    SupplierCode map(final SupplierCodeDto supplierCode);

    ShipmentId map(final ShipmentIdDto shipmentId);

}
