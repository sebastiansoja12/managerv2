package com.warehouse.terminal.domain.vo;

import java.util.List;
import java.util.Objects;


public class Response {
    private final Long terminalId;
    private final String version;
    private final Long userId;
    private final List<ProcessReturn> processReturns;
    private final List<RouteProcess> routeProcesses;

    public Response(final DeviceInformation deviceInformation,
                    final List<ProcessReturn> processReturns,
                    final List<RouteProcess> routeProcesses) {
        Objects.requireNonNull(deviceInformation);
        this.terminalId = deviceInformation.getDeviceId();
        this.version = deviceInformation.getVersion();
        this.userId = deviceInformation.getUserId();
        this.processReturns = processReturns;
        this.routeProcesses = routeProcesses;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public String getVersion() {
        return version;
    }

    public List<ProcessReturn> getProcessReturns() {
        return processReturns;
    }

    public List<RouteProcess> getRouteProcesses() {
        return routeProcesses;
    }

    public Long getUserId() {
        return userId;
    }
}

