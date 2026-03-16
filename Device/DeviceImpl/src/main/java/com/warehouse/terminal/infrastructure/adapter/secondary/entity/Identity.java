package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class Identity {

    @Column(name = "hardware_uuid")
    private UUID hardwareUuid;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "imei")
    private String imei;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "asset_tag")
    private String assetTag;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "external_system_id")
    private String externalSystemId;

    @Column(name = "mdm_device_id")
    private String mdmDeviceId;

    protected Identity() {}

	public Identity(final UUID hardwareUuid, final String serialNumber, final String imei, final String macAddress,
			final String assetTag, final String barcode, final String externalSystemId, final String mdmDeviceId) {
		this.hardwareUuid = hardwareUuid;
		this.serialNumber = serialNumber;
		this.imei = imei;
		this.macAddress = macAddress;
		this.assetTag = assetTag;
		this.barcode = barcode;
		this.externalSystemId = externalSystemId;
		this.mdmDeviceId = mdmDeviceId;
	}

    public String getAssetTag() {
        return assetTag;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getExternalSystemId() {
        return externalSystemId;
    }

    public UUID getHardwareUuid() {
        return hardwareUuid;
    }

    public String getImei() {
        return imei;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getMdmDeviceId() {
        return mdmDeviceId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
}

