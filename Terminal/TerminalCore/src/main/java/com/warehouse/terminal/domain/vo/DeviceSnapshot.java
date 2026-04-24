package com.warehouse.terminal.domain.vo;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.device.Scanner;

public record DeviceSnapshot(
        DeviceId deviceId,
        ExternalId<String> externalDeviceId,
        DeviceType deviceType,
        DeviceStatus status,
        IdentityInfo identity,
        HardwareProfile hardware,
        SoftwareProfile software,
        NetworkProfile network,
        SecurityProfile security,
        LocationProfile location,
        OwnershipProfile ownership,
        UserId userId,
        DepartmentCode departmentCode,
        String version,
        Instant createdAt,
        Instant updatedAt,
        Instant lastUpdate,
        Boolean active,
        Scanner.ScanType scanType,
        Scanner.ScannerType scannerType
) {

    public static DeviceSnapshot from(final Device device) {
        return device.toSnapshot();
    }
}
