package com.warehouse.commonassets.vo;

import com.warehouse.commonassets.response.LogStatus;


public class RouteProcess {
    private final Long parcelId;
    private final LogStatus logStatus;

    public RouteProcess(Long parcelId, LogStatus logStatus) {
        this.parcelId = parcelId;
        this.logStatus = logStatus;
    }

    public Long getParcelId() {
        return parcelId;
    }

    public LogStatus getLogStatus() {
        return logStatus;
    }
}
