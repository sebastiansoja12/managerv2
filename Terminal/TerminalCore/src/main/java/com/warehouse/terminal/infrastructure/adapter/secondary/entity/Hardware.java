package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Hardware {

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "model")
    private String model;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "cpu")
    private String cpu;

    @Column(name = "ram_mb")
    private Integer ramMb;

    @Column(name = "storage_mb")
    private Integer storageMb;

    @Column(name = "screen_resolution")
    private String screenResolution;

    @Column(name = "has_scanner")
    private Boolean hasScanner;

    @Column(name = "has_camera")
    private Boolean hasCamera;

    @Column(name = "has_nfc")
    private Boolean hasNfc;

    @Column(name = "has_gps")
    private Boolean hasGps;

    @Column(name = "ruggedized")
    private Boolean ruggedized;

    protected Hardware() {}

	public Hardware(final String manufacturer, final String model, final String productName, final String cpu,
			final Integer ramMb, final Integer storageMb, final String screenResolution, final Boolean hasScanner,
			final Boolean hasCamera, final Boolean hasNfc, final Boolean hasGps, final Boolean ruggedized) {
		this.manufacturer = manufacturer;
		this.model = model;
		this.productName = productName;
		this.cpu = cpu;
		this.ramMb = ramMb;
		this.storageMb = storageMb;
		this.screenResolution = screenResolution;
		this.hasScanner = hasScanner;
		this.hasCamera = hasCamera;
		this.hasNfc = hasNfc;
		this.hasGps = hasGps;
		this.ruggedized = ruggedized;
	}

    public String getCpu() {
        return cpu;
    }

    public Boolean getHasCamera() {
        return hasCamera;
    }

    public Boolean getHasGps() {
        return hasGps;
    }

    public Boolean getHasNfc() {
        return hasNfc;
    }

    public Boolean getHasScanner() {
        return hasScanner;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getRamMb() {
        return ramMb;
    }

    public Boolean getRuggedized() {
        return ruggedized;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public Integer getStorageMb() {
        return storageMb;
    }
}

