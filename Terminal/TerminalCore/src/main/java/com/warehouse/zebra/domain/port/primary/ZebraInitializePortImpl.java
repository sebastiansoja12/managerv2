package com.warehouse.zebra.domain.port.primary;



import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;
import com.warehouse.commonassets.vo.RouteProcess;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;

import lombok.AllArgsConstructor;

import static com.warehouse.commonassets.enumeration.ProcessType.CREATED;

@AllArgsConstructor
public class ZebraInitializePortImpl implements ZebraInitializePort {

    private final RouteLogServicePort routeLogServicePort;

    @Override
    public Response processRequest(final Request request) {
        final ProcessType processType = request.getProcessType();
        if (!CREATED.equals(processType)) {
            throw new RuntimeException("Invalid Process Type");
        }

        final List<RouteProcess> routeProcesses = request.getParcelCreatedRequests()
                .stream()
                .map(parcelCreatedRequest -> routeLogServicePort.initializeProcess(parcelCreatedRequest.getParcelId()))
                .collect(Collectors.toList());

        return new Response(request.getZebraDeviceInformation(), Collections.emptyList(), routeProcesses);
    }
}
