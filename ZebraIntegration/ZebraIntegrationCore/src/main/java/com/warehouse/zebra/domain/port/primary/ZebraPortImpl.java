package com.warehouse.zebra.domain.port.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;
import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;

import lombok.AllArgsConstructor;

import java.util.Collections;

@AllArgsConstructor
public class ZebraPortImpl implements ZebraPort {

    private final ReturnServicePort returnServicePort;

	private final RouteLogServicePort routeLogServicePort;

	@Override
	public Response processRequest(final Request request) {
		final ProcessType processType = request.getProcessType();
		if (ObjectUtils.isEmpty(processType)) {
			throw new RuntimeException("Invalid Process Type");
		}

		return switch (processType) {
		case RETURN -> returnServicePort.processReturn(request);
		case REJECT, ROUTE, REROUTE, REDIRECT, CREATED, MISS ->
			new Response(request.getZebraDeviceInformation(), Collections.emptyList(), Collections.emptyList());
		};
    }
}
