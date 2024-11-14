package com.warehouse.terminal.domain.model.request;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.dto.DeviceTypeDto;
import com.warehouse.terminal.request.TerminalAddRequestDto;

public class TerminalAddRequest {
    private String username;
    private String version;
    private String depotCode;
    private DeviceType deviceType;

	public TerminalAddRequest(final String username, final String version, final String depotCode,
			final DeviceType deviceType) {
        this.username = username;
        this.version = version;
        this.depotCode = depotCode;
        this.deviceType = deviceType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(final String depotCode) {
        this.depotCode = depotCode;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public static TerminalAddRequest from(final TerminalAddRequestDto terminalAddRequest) {
        return new TerminalAddRequest(terminalAddRequest.username().value(),
                terminalAddRequest.version().value(), terminalAddRequest.depotCode().value(),
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