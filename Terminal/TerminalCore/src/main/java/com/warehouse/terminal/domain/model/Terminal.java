package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;

import java.time.Instant;

public class Terminal {
    private TerminalId terminalId;
    private DeviceType deviceType;
    private UserId userId;
    private String depotCode;
    private String version;
    private Instant lastUpdate;

	public Terminal(final TerminalId terminalId,
                    final DeviceType deviceType,
                    final UserId userId,
                    final String depotCode,
                    final String version,
                    final Instant lastUpdate) {
        this.terminalId = terminalId;
        this.deviceType = deviceType;
        this.userId = userId;
        this.depotCode = depotCode;
        this.version = version;
        this.lastUpdate = lastUpdate;
    }

    public static Terminal from(final TerminalAddRequest request, final UserId userId) {
        return new Terminal(null, request.getDeviceType(), userId, request.getDepotCode(), request.getVersion(), Instant.now());
    }

    public TerminalId getTerminalId() {
        return terminalId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public String getVersion() {
        return version;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void updateUserId(final UserId userId) {
        this.userId = userId;
        markAsModified();
    }

    public void updateDepotCode(final String depotCode) {
        this.depotCode = depotCode;
        markAsModified();
    }

    public void updateDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
        markAsModified();
    }

    private void markAsModified() {
        this.lastUpdate = Instant.now();
    }

    public void updateVersion(final String version) {
        this.version = version;
        markAsModified();
    }
}
