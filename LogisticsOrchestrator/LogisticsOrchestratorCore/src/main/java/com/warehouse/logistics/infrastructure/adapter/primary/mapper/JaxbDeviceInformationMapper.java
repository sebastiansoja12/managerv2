package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.DeviceUserType;

public final class JaxbDeviceInformationMapper {

    private JaxbDeviceInformationMapper() {
    }

    public static DeviceInformation map(final com.warehouse.terminal.jaxb.DeviceType device) {
        return DeviceInformation.builder()
                .departmentCode(new DepartmentCode(device.getDepartmentCode()))
                .deviceId(new DeviceId(device.getDeviceID()))
                .username(device.getResponsibleUser())
                .version(device.getVersion())
                .deviceType(DeviceType.valueOf(device.getDeviceType()))
                .deviceUserType(DeviceUserType.valueOf(device.getDeviceUserType().name()))
                .build();
    }
}
