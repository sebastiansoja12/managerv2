package com.warehouse.process.domain.model;

import lombok.Builder;

@Builder
public class InitializeProcessCommand {
    private String request;
    private DeviceInformation deviceInformation;

    public String request() {
        return request;
    }

    public DeviceInformation deviceInformation() {
        return deviceInformation;
    }
}
