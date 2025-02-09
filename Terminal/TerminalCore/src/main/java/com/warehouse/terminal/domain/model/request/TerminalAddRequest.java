package com.warehouse.terminal.domain.model.request;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.dto.DeviceTypeDto;
import com.warehouse.terminal.request.TerminalAddRequestDto;

public class TerminalAddRequest {
    private Username username;
    private String version;
    private String departmentCode;
    private DeviceType deviceType;

	public TerminalAddRequest(final Username username, final String version, final String departmentCode,
			final DeviceType deviceType) {
        this.username = username;
        this.version = version;
        this.departmentCode = departmentCode;
        this.deviceType = deviceType;
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(final Username username) {
        this.username = username;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public static TerminalAddRequest from(final TerminalAddRequestDto terminalAddRequest) {
        return new TerminalAddRequest(new Username(terminalAddRequest.username().value()),
                terminalAddRequest.version().value(), terminalAddRequest.departmentCode().value(),
                determineDeviceType(terminalAddRequest.deviceType()));
    }

    private static DeviceType determineDeviceType(final DeviceTypeDto deviceType) {
        return switch (deviceType) {
            case MANAGER -> DeviceType.MANAGER;
            case SUPPLIER -> DeviceType.SUPPLIER;
            case TERMINAL -> DeviceType.TERMINAL;
        };
    }

}
