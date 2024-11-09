package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.terminal.domain.enumeration.DeviceType;

public class Terminal {
    private TerminalId terminalId;
    private DeviceType deviceType;
    private String username;
    private String depotCode;
    private String version;

	public Terminal(final TerminalId terminalId, final DeviceType deviceType, final String username,
			final String depotCode, final String version) {
        this.terminalId = terminalId;
        this.deviceType = deviceType;
        this.username = username;
        this.depotCode = depotCode;
        this.version = version;
    }

    public TerminalId getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(final TerminalId terminalId) {
        this.terminalId = terminalId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(final String depotCode) {
        this.depotCode = depotCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }
}
