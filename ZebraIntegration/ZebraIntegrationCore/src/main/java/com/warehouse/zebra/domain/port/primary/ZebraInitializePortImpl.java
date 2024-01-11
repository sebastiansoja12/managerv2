package com.warehouse.zebra.domain.port.primary;


import static com.warehouse.zebra.domain.vo.ProcessType.CREATED;

import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebra.domain.vo.ProcessType;
import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.Response;
import com.warehouse.zebra.domain.vo.RouteProcess;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ZebraInitializePortImpl implements ZebraInitializePort {

    private final RouteLogServicePort routeLogServicePort;

    @Override
    public Response processRequest(Request request) {
        final ProcessType processType = request.getProcessType();
        if (!CREATED.equals(processType)) {
            throw new RuntimeException("Invalid Process Type");
        }

        final List<RouteProcess> routeProcesses = request.getParcelCreatedRequests()
                .stream()
                .map(parcelCreatedRequest -> routeLogServicePort.initializeProcess(parcelCreatedRequest.getParcelId()))
                .collect(Collectors.toList());

        return Response.builder()
                .zebraId(request.getZebraDeviceInformation().getZebraId())
                .version(request.getZebraDeviceInformation().getVersion())
                .username(request.getZebraDeviceInformation().getUsername())
                .routeProcesses(routeProcesses)
                .build();
    }
}
