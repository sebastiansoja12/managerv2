package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "terminal_pair")
public class TerminalPairEntity {

    @Id
    @Column(name = "terminal_pair_id", nullable = false)
    private Long terminalPairId;

    @OneToOne(fetch = FetchType.LAZY)
    private DeviceEntity deviceEntity;

    @Column(name = "paired")
    private boolean paired;

    @Column(name = "pair_time")
    private Instant loginTime;

    @Column(name = "error_description")
    private String errorDescription;

    public TerminalPairEntity() {
    }

    public TerminalPairEntity(final DeviceEntity deviceEntity) {
        this.deviceEntity = deviceEntity;
        this.paired = true;
        this.loginTime = Instant.now();
        this.errorDescription = "";
    }

	public TerminalPairEntity(final Long terminalPairId,
                              final DeviceEntity deviceEntity,
                              final boolean paired,
                              final Instant loginTime,
                              final String errorDescription) {
        this.terminalPairId = terminalPairId;
        this.deviceEntity = deviceEntity;
        this.paired = paired;
        this.loginTime = loginTime;
        this.errorDescription = errorDescription;
    }

    public TerminalPairEntity(final DeviceEntity deviceEntity, final String errorDescription) {
        this.deviceEntity = deviceEntity;
        this.paired = false;
        this.loginTime = Instant.now();
        this.errorDescription = errorDescription;
    }

    public Long getTerminalPairId() {
        return terminalPairId;
    }

    public void setTerminalPairId(final Long terminalPairId) {
        this.terminalPairId = terminalPairId;
    }

    public DeviceEntity getDeviceEntity() {
        return deviceEntity;
    }

    public void setDeviceEntity(final DeviceEntity deviceEntity) {
        this.deviceEntity = deviceEntity;
    }

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(final boolean paired) {
        this.paired = paired;
    }

    public Instant getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(final Instant loginTime) {
        this.loginTime = loginTime;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(final String errorDescription) {
        this.errorDescription = errorDescription;
    }
}

