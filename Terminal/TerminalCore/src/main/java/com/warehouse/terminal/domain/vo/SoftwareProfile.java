package com.warehouse.terminal.domain.vo;

import java.util.Set;

public class SoftwareProfile {
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

    public SoftwareProfile() {}

	public SoftwareProfile(final String appVersion, final String bootloaderVersion, final String buildNumber,
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
}

