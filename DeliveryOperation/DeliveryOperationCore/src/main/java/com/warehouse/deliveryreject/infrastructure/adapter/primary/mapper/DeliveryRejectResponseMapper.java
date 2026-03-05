package com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.DeviceIdDto;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.DeviceTypeDto;
import com.warehouse.delivery.dto.DeviceUserTypeDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.delivery.dto.SupplierCodeDto;
import com.warehouse.delivery.dto.UsernameDto;
import com.warehouse.delivery.dto.VersionDto;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponseDetails;
import org.mapstruct.Mapper;

import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.dto.DeliveryRejectResponseDetailsDto;
import com.warehouse.deliveryreject.dto.RejectReasonDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.DeviceUserType;

@Mapper
public interface DeliveryRejectResponseMapper {

    DeliveryRejectResponseDto map(final DeliveryRejectResponse response);

    default DeliveryRejectResponseDetailsDto map(final DeliveryRejectResponseDetails details) {
        if (details == null) {
            return null;
        }
        return new DeliveryRejectResponseDetailsDto(
                map(details.getShipmentId()),
                map(details.getNewShipmentId()),
                map(details.getDeliveryStatus()),
                map(details.getDepartmentCode()),
                map(details.getSupplierCode()),
                map(details.getRejectReason()));
    }

    default DeviceInformationDto map(final DeviceInformation deviceInformation) {
        if (deviceInformation == null) {
            return null;
        }
        return new DeviceInformationDto(
                map(deviceInformation.getDeviceId()),
                map(deviceInformation.getDepartmentCode()),
                mapVersion(deviceInformation.getVersion()),
                mapUsername(deviceInformation.getUsername()),
                map(deviceInformation.getDeviceUserType()),
                map(deviceInformation.getDeviceType()));
    }

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        return shipmentId != null ? new ShipmentIdDto(shipmentId.getValue()) : null;
    }

    default DepartmentCodeDto map(final DepartmentCode departmentCode) {
        return departmentCode != null ? new DepartmentCodeDto(departmentCode.getValue()) : null;
    }

    default DeviceIdDto map(final DeviceId deviceId) {
        return deviceId != null ? new DeviceIdDto(deviceId.getValue()) : null;
    }

    default SupplierCodeDto map(final SupplierCode supplierCode) {
        return supplierCode != null ? new SupplierCodeDto(supplierCode.value()) : null;
    }

    default RejectReasonDto map(final RejectReason rejectReason) {
        return rejectReason != null ? new RejectReasonDto(rejectReason.getValue()) : null;
    }

    default DeliveryStatusDto map(final DeliveryStatus deliveryStatus) {
        return deliveryStatus != null ? DeliveryStatusDto.valueOf(deliveryStatus.name()) : null;
    }

    default DeviceUserTypeDto map(final DeviceUserType deviceUserType) {
        return deviceUserType != null ? DeviceUserTypeDto.valueOf(deviceUserType.name()) : null;
    }

    default DeviceTypeDto map(final DeviceType deviceType) {
        return deviceType != null ? DeviceTypeDto.valueOf(deviceType.name()) : null;
    }

    default VersionDto mapVersion(final String version) {
        return version != null ? new VersionDto(version) : null;
    }

    default UsernameDto mapUsername(final String username) {
        return username != null ? new UsernameDto(username) : null;
    }
}
