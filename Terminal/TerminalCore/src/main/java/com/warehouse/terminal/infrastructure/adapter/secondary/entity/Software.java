package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Software {

    @Column(name = "os_name")
    private String osName;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "firmware_version")
    private String firmwareVersion;

    @Column(name = "kernel_version")
    private String kernelVersion;

    @Column(name = "bootloader_version")
    private String bootloaderVersion;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "build_number")
    private String buildNumber;

    @Column(name = "rooted")
    private Boolean rooted;

    @Column(name = "developer_mode_enabled")
    private Boolean developerModeEnabled;

    protected Software() {}

    public Software(final String osName,
                    final String osVersion,
                    final String firmwareVersion,
                    final String kernelVersion,
                    final String bootloaderVersion,
                    final String appVersion,
                    final String buildNumber,
                    final Boolean rooted,
                    final Boolean developerModeEnabled) {
        this.osName = osName;
        this.osVersion = osVersion;
        this.firmwareVersion = firmwareVersion;
        this.kernelVersion = kernelVersion;
        this.bootloaderVersion = bootloaderVersion;
        this.appVersion = appVersion;
        this.buildNumber = buildNumber;
        this.rooted = rooted;
        this.developerModeEnabled = developerModeEnabled;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getBootloaderVersion() {
        return bootloaderVersion;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public Boolean getDeveloperModeEnabled() {
        return developerModeEnabled;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public Boolean getRooted() {
        return rooted;
    }
}
