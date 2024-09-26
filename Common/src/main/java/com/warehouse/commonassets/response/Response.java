package com.warehouse.commonassets.response;

import java.util.List;
import java.util.Objects;

import com.warehouse.commonassets.vo.DeviceInformation;
import com.warehouse.commonassets.vo.ProcessReturn;
import com.warehouse.commonassets.vo.RouteProcess;


public class Response {
    private final Long zebraId;
    private final String version;
    private final String username;
    private final List<ProcessReturn> processReturns;
    private final List<RouteProcess> routeProcesses;

    public Response(final DeviceInformation deviceInformation,
                    final List<ProcessReturn> processReturns,
                    final List<RouteProcess> routeProcesses) {
        Objects.requireNonNull(deviceInformation);
        this.zebraId = deviceInformation.getZebraId();
        this.version = deviceInformation.getVersion();
        this.username = deviceInformation.getUsername();
        this.processReturns = processReturns;
        this.routeProcesses = routeProcesses;
    }

    public Long getZebraId() {
        return zebraId;
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

