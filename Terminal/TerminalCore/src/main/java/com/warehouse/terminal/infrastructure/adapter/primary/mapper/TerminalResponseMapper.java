package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.vo.DeviceInformationResponse;
import com.warehouse.terminal.domain.vo.DevicePairResponse;
import com.warehouse.terminal.dto.DepartmentCodeDto;
import com.warehouse.terminal.dto.DeviceDto;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.DeviceTypeDto;
import com.warehouse.terminal.dto.UsernameDto;
import com.warehouse.terminal.dto.VersionDto;
import com.warehouse.terminal.response.DeviceInformationResponseDto;
import com.warehouse.terminal.response.DevicePairResponseDto;

@Mapper
public interface TerminalResponseMapper {

    default DeviceDto mapToDeviceResponse(final Device device) {
        final DeviceIdDto deviceId = new DeviceIdDto(device.getDeviceId() != null ? device.getDeviceId().value() : null);
        final DeviceTypeDto deviceType = map(device.getDeviceType());

        final VersionDto version = version(device);
        final UsernameDto username = username(device);
        final DepartmentCodeDto departmentCode = departmentCode(device);
        final Instant lastUpdate = lastUpdate(device);
        final Boolean active = active(device);

        return new DeviceDto(deviceId, version, deviceType, username, departmentCode, lastUpdate, active);
    }

    default VersionDto version(final Device device) {
        try {
            return device.getVersion() != null ? new VersionDto(device.getVersion()) : null;
        } catch (final UnsupportedOperationException ignored) {
            return null;
        }
    }

    default UsernameDto username(final Device device) {
        try {
            return device.getUserId() != null ? new UsernameDto(String.valueOf(device.getUserId().value())) : null;
        } catch (final UnsupportedOperationException ignored) {
            return null;
        }
    }

    default DepartmentCodeDto departmentCode(final Device device) {
        try {
            return device.getDepartmentCode() != null ? new DepartmentCodeDto(device.getDepartmentCode().value()) : null;
        } catch (final UnsupportedOperationException ignored) {
            return null;
        }
    }

    default Instant lastUpdate(final Device device) {
        try {
            final Instant value = device.getLastUpdate();
            return value != null ? value : device.getUpdatedAt();
        } catch (final UnsupportedOperationException ignored) {
            return device.getUpdatedAt();
        }
    }

    default Boolean active(final Device device) {
        try {
            return device.isActive();
        } catch (final UnsupportedOperationException ignored) {
            return null;
        }
    }

    DeviceTypeDto map(final DeviceType deviceType);

    DeviceInformationResponseDto map(final DeviceInformationResponse deviceInformationResponse);

    DevicePairResponseDto map(final DevicePairResponse devicePairResponse);
}
