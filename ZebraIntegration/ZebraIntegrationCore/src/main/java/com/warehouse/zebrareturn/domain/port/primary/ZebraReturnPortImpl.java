package com.warehouse.zebrareturn.domain.port.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;
import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.zebrareturn.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebrareturn.domain.port.secondary.RouteLogServicePort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ZebraReturnPortImpl implements ZebraReturnPort {

    private final ReturnServicePort returnServicePort;

	private final RouteLogServicePort routeLogServicePort;

	@Override
	public Response processRequest(final Request request) {
		final ProcessType processType = request.getProcessType();
		if (ObjectUtils.isEmpty(processType)) {
			throw new RuntimeException("Invalid Process Type");
		}
		return returnServicePort.processReturn(request);
    }
}
