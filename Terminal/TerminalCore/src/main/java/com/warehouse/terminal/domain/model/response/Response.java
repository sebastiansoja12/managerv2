package com.warehouse.terminal.domain.model.response;

import java.util.List;
import java.util.Objects;

import com.warehouse.terminal.domain.model.vo.DeviceInformation;
import com.warehouse.terminal.domain.vo.ProcessReturn;
import com.warehouse.terminal.domain.vo.RouteProcess;


public class Response {
    private final Long terminalId;
    private final String version;
    private final String username;
    private final List<ProcessReturn> processReturns;
    private final List<RouteProcess> routeProcesses;

    public Response(final DeviceInformation deviceInformation,
                    final List<ProcessReturn> processReturns,
                    final List<RouteProcess> routeProcesses) {
        Objects.requireNonNull(deviceInformation);
        this.terminalId = deviceInformation.getTerminalId();
        this.version = deviceInformation.getVersion();
        this.username = deviceInformation.getUsername();
        this.processReturns = processReturns;
        this.routeProcesses = routeProcesses;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public String getVersion() {
        return version;
    }

    public String getUsername() {
        return username;
    }

    public List<ProcessReturn> getProcessReturns() {
        return processReturns;
    }

    public List<RouteProcess> getRouteProcesses() {
        return routeProcesses;
    }
}

