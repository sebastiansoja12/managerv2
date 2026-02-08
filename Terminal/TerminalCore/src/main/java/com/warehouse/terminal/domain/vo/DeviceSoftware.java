package com.warehouse.terminal.domain.vo;

import java.util.Set;

public class DeviceSoftware {
    private String osName;
    private String osVersion;
    private String firmwareVersion;
    private String kernelVersion;
    private String bootloaderVersion;
    private String appVersion;
    private String buildNumber;
    private Boolean rooted;
    private Boolean developerModeEnabled;
    private Set<String> installedApps;

    public DeviceSoftware() {}

	public DeviceSoftware(final String appVersion, final String bootloaderVersion, final String buildNumber,
                          final Boolean developerModeEnabled, final String firmwareVersion, final Set<String> installedApps,
                          final String kernelVersion, final String osName, final String osVersion, final Boolean rooted) {
        this.appVersion = appVersion;
        this.bootloaderVersion = bootloaderVersion;
        this.buildNumber = buildNumber;
        this.developerModeEnabled = developerModeEnabled;
        this.firmwareVersion = firmwareVersion;
        this.installedApps = installedApps;
        this.kernelVersion = kernelVersion;
        this.osName = osName;
        this.osVersion = osVersion;
        this.rooted = rooted;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBootloaderVersion() {
        return bootloaderVersion;
    }

    public void setBootloaderVersion(final String bootloaderVersion) {
        this.bootloaderVersion = bootloaderVersion;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(final String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public Boolean getDeveloperModeEnabled() {
        return developerModeEnabled;
    }

    public void setDeveloperModeEnabled(final Boolean developerModeEnabled) {
        this.developerModeEnabled = developerModeEnabled;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(final String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public Set<String> getInstalledApps() {
        return installedApps;
    }

    public void setInstalledApps(final Set<String> installedApps) {
        this.installedApps = installedApps;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(final String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(final String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(final String osVersion) {
        this.osVersion = osVersion;
    }

    public Boolean getRooted() {
        return rooted;
    }

    public void setRooted(final Boolean rooted) {
        this.rooted = rooted;
    }

    public void update(final DeviceSoftware software) {
        if (software.getOsName() != null) {
            this.osName = software.getOsName();
        }
        if (software.getOsVersion() != null) {
            this.osVersion = software.getOsVersion();
        }
        if (software.getFirmwareVersion() != null) {
            this.firmwareVersion = software.getFirmwareVersion();
        }
        if (software.getKernelVersion() != null) {
            this.kernelVersion = software.getKernelVersion();
        }
        if (software.getBootloaderVersion() != null) {
            this.bootloaderVersion = software.getBootloaderVersion();
        }
        if (software.getAppVersion() != null) {
            this.appVersion = software.getAppVersion();
        }
        if (software.getBuildNumber() != null) {
            this.buildNumber = software.getBuildNumber();
        }
        if (software.getRooted() != null) {
            this.rooted = software.getRooted();
        }
        if (software.getDeveloperModeEnabled() != null) {
            this.developerModeEnabled = software.getDeveloperModeEnabled();
        }
        if (software.getInstalledApps() != null) {
            this.installedApps = software.getInstalledApps();
        }
    }
}
