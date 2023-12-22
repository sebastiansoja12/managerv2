package com.warehouse.zebra.domain.port.primary;

import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebra.domain.vo.*;
import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ZebraPortImpl implements ZebraPort {

    private final ReturnServicePort returnServicePort;

	private final RouteLogServicePort routeLogServicePort;

	@Override
	public Response processRequest(Request request) {
		final ProcessType processType = request.getProcessType();
		if (ObjectUtils.isEmpty(processType)) {
			throw new RuntimeException("Invalid Process Type");
		}

		return switch (processType) {
			case CREATED -> processCreated(request);
			case RETURN -> returnServicePort.processReturn(request);
			case REJECT, ROUTE, REROUTE, REDIRECT -> Response.builder()
					.zebraId(request.getZebraDeviceInformation().getZebraId())
					.version(request.getZebraDeviceInformation().getVersion())
					.username(request.getZebraDeviceInformation().getUsername())
					.build();
		};
    }

	private Response processCreated(Request request) {
		final List<RouteProcess> routeProcesses = new ArrayList<>();
		request.getCreatedRequests().forEach(req -> {
			final RouteProcess routeProcess = routeLogServicePort.initializeProcess(req.getParcelId());
			routeProcesses.add(routeProcess);
		});

		return Response.builder()
				.zebraId(request.getZebraDeviceInformation().getZebraId())
				.version(request.getZebraDeviceInformation().getVersion())
				.username(request.getZebraDeviceInformation().getUsername())
				.routeProcesses(routeProcesses)
				.build();
	}
}
