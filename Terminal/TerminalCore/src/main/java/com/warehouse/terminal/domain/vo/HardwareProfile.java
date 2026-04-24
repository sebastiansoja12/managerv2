package com.warehouse.terminal.domain.vo;

public class HardwareProfile {
	private String manufacturer;
	private String model;
	private String productName;
	private String cpu;
	private Integer ramMb;
	private Integer storageMb;
	private String screenResolution;
	private Boolean hasScanner;
	private Boolean hasCamera;
	private Boolean hasNfc;
	private Boolean hasGps;
	private Boolean ruggedized;

    public HardwareProfile() {}

	public HardwareProfile(final String cpu, final Boolean hasCamera, final Boolean hasGps, final Boolean hasNfc,
			final Boolean hasScanner, final String manufacturer, final String model, final String productName,
			final Integer ramMb, final Boolean ruggedized, final String screenResolution, final Integer storageMb) {
		this.cpu = cpu;
		this.hasCamera = hasCamera;
		this.hasGps = hasGps;
		this.hasNfc = hasNfc;
		this.hasScanner = hasScanner;
		this.manufacturer = manufacturer;
		this.model = model;
		this.productName = productName;
		this.ramMb = ramMb;
		this.ruggedized = ruggedized;
		this.screenResolution = screenResolution;
		this.storageMb = storageMb;
	}

    public String getCpu() {
        return cpu;
    }

    public void setCpu(final String cpu) {
        this.cpu = cpu;
    }

    public Boolean getHasCamera() {
        return hasCamera;
    }

    public void setHasCamera(final Boolean hasCamera) {
        this.hasCamera = hasCamera;
    }

    public Boolean getHasGps() {
        return hasGps;
    }

    public void setHasGps(final Boolean hasGps) {
        this.hasGps = hasGps;
    }

    public Boolean getHasNfc() {
        return hasNfc;
    }

    public void setHasNfc(final Boolean hasNfc) {
        this.hasNfc = hasNfc;
    }

    public Boolean getHasScanner() {
        return hasScanner;
    }

    public void setHasScanner(final Boolean hasScanner) {
        this.hasScanner = hasScanner;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(final String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public Integer getRamMb() {
        return ramMb;
    }

    public void setRamMb(final Integer ramMb) {
        this.ramMb = ramMb;
    }

    public Boolean getRuggedized() {
        return ruggedized;
    }

    public void setRuggedized(final Boolean ruggedized) {
        this.ruggedized = ruggedized;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(final String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public Integer getStorageMb() {
        return storageMb;
    }

    public void setStorageMb(final Integer storageMb) {
        this.storageMb = storageMb;
    }

    public void update(final HardwareProfile hardware) {
        if (hardware != null) {
            if (hardware.getManufacturer() != null) {
                this.manufacturer = hardware.getManufacturer();
            }
            if (hardware.getModel() != null) {
                this.model = hardware.getModel();
            }
            if (hardware.getProductName() != null) {
                this.productName = hardware.getProductName();
            }
            if (hardware.getCpu() != null) {
                this.cpu = hardware.getCpu();
            }
            if (hardware.getRamMb() != null) {
                this.ramMb = hardware.getRamMb();
            }
            if (hardware.getStorageMb() != null) {
                this.storageMb = hardware.getStorageMb();
            }
            if (hardware.getScreenResolution() != null) {
                this.screenResolution = hardware.getScreenResolution();
            }
            if (hardware.getHasScanner() != null) {
                this.hasScanner = hardware.getHasScanner();
            }
            if (hardware.getHasCamera() != null) {
                this.hasCamera = hardware.getHasCamera();
            }
            if (hardware.getHasNfc() != null) {
                this.hasNfc = hardware.getHasNfc();
            }
            if (hardware.getHasGps() != null) {
                this.hasGps = hardware.getHasGps();
            }
            if (hardware.getRuggedized() != null) {
                this.ruggedized = hardware.getRuggedized();
            }
        }
    }
}
