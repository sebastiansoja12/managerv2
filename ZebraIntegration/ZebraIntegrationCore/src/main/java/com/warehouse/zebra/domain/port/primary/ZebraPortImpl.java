package com.warehouse.zebra.domain.port.primary;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.domain.vo.ProcessType;
import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ZebraPortImpl implements ZebraPort {

    private final ReturnServicePort returnServicePort;

	@Override
	public Response processRequest(Request request) {
		final ProcessType processType = request.getProcessType();
		if (ObjectUtils.isEmpty(processType)) {
			throw new RuntimeException("Invalid Process Type");
		}
		return switch (processType) {
			case RETURN -> returnServicePort.processReturn(request);
			case REJECT, ROUTE, REROUTE, REDIRECT, CREATED -> Response.builder()
					.zebraId(request.getZebraDeviceInformation().getZebraId())
					.version(request.getZebraDeviceInformation().getVersion())
					.username(request.getZebraDeviceInformation().getUsername())
					.build();
		};
    }
}
