package com.warehouse.terminal.domain.vo;

import java.util.UUID;

public class IdentityInfo {
    private UUID hardwareUuid;
    private String serialNumber;
    private String imei;
    private String macAddress;
    private String assetTag;
    private String barcode;
    private String externalSystemId;
    private String mdmDeviceId;

    public IdentityInfo() {

    }

	public IdentityInfo(final String assetTag, final String barcode, final String externalSystemId,
			final UUID hardwareUuid, final String imei, final String macAddress, final String mdmDeviceId,
			final String serialNumber) {
        this.assetTag = assetTag;
        this.barcode = barcode;
        this.externalSystemId = externalSystemId;
        this.hardwareUuid = hardwareUuid;
        this.imei = imei;
        this.macAddress = macAddress;
        this.mdmDeviceId = mdmDeviceId;
        this.serialNumber = serialNumber;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(final String assetTag) {
        this.assetTag = assetTag;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public String getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(final String externalSystemId) {
        this.externalSystemId = externalSystemId;
    }

    public UUID getHardwareUuid() {
        return hardwareUuid;
    }

    public void setHardwareUuid(final UUID hardwareUuid) {
        this.hardwareUuid = hardwareUuid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(final String imei) {
        this.imei = imei;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMdmDeviceId() {
        return mdmDeviceId;
    }

    public void setMdmDeviceId(final String mdmDeviceId) {
        this.mdmDeviceId = mdmDeviceId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void update(final IdentityInfo identity) {
        if (identity != null) {
            if (identity.getHardwareUuid() != null) {
                this.hardwareUuid = identity.getHardwareUuid();
            }
            if (identity.getSerialNumber() != null) {
                this.serialNumber = identity.getSerialNumber();
            }
            if (identity.getImei() != null) {
                this.imei = identity.getImei();
            }
            if (identity.getMacAddress() != null) {
                this.macAddress = identity.getMacAddress();
            }
            if (identity.getAssetTag() != null) {
                this.assetTag = identity.getAssetTag();
            }
            if (identity.getBarcode() != null) {
                this.barcode = identity.getBarcode();
            }
            if (identity.getExternalSystemId() != null) {
                this.externalSystemId = identity.getExternalSystemId();
            }
            if (identity.getMdmDeviceId() != null) {
                this.mdmDeviceId = identity.getMdmDeviceId();
            }
        }
    }
}
