package com.warehouse.terminal.domain.model.command;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.vo.HardwareProfile;
import com.warehouse.terminal.domain.vo.IdentityInfo;
import com.warehouse.terminal.domain.vo.LocationProfile;
import com.warehouse.terminal.domain.vo.NetworkProfile;
import com.warehouse.terminal.domain.vo.SecurityProfile;
import com.warehouse.terminal.domain.vo.SoftwareProfile;

public record DeviceUpdateCommand(
        DeviceId deviceId,
        ExternalId<String> externalDeviceId,
        DeviceType deviceType,
        DeviceStatus status,
        UserId userId,
        DepartmentCode departmentCode,
        String version,
        Instant createdAt,
        Instant updatedAt,
        Instant lastUpdate,
        Boolean active,
        IdentityInfo identity,
        HardwareProfile hardware,
        SoftwareProfile software,
        NetworkProfile network,
        SecurityProfile security,
        LocationProfile location,
        OwnershipProfile ownership
) {
}
