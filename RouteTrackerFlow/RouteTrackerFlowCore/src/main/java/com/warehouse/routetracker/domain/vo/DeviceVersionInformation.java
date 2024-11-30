package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class DeviceVersionInformation {
    private String version;
    private ShipmentId shipmentId;
    private ProcessType processType;

	public DeviceVersionInformation(final String version, final ShipmentId shipmentId,
                                    final ProcessType processType) {
        this.version = version;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId parcelId) {
        this.shipmentId = parcelId;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }
}
