package com.warehouse.terminal.domain.vo;

import com.warehouse.terminal.domain.model.response.LogStatus;


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
