package com.warehouse.zebrareturn.domain.port.primary;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;
import com.warehouse.zebrareturn.domain.port.secondary.ReturnServicePort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ZebraReturnPortImpl implements ZebraReturnPort {

    private final ReturnServicePort returnServicePort;

	@Override
	public Response processRequest(final Request request) {
		final ProcessType processType = request.getProcessType();
		if (ObjectUtils.isEmpty(processType)) {
			throw new RuntimeException("Invalid Process Type");
		}
		return returnServicePort.processReturn(request);
    }
}