package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.ParcelId;

import java.util.UUID;


public class RouteProcess {
    private final ParcelId parcelId;
    private final UUID processId;

    public RouteProcess(final ParcelId parcelId, final UUID processId) {
        this.parcelId = parcelId;
        this.processId = processId;
    }

    public ParcelId getParcelId() {
        return parcelId;
    }

    public UUID getProcessId() {
        return processId;
    }

    public static RouteProcess from(final ParcelId parcelId, final UUID processId) {
        return new RouteProcess(parcelId, processId);
    }
}
