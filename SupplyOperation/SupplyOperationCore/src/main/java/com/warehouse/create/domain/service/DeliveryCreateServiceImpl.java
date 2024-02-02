package com.warehouse.create.domain.service;


import com.warehouse.create.domain.model.DeliveryCreate;
import com.warehouse.create.domain.model.Request;
import com.warehouse.create.domain.model.Response;
import com.warehouse.create.domain.port.secondary.DeliveryCreateRepository;
import com.warehouse.create.domain.port.secondary.RouteLogServicePort;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class DeliveryCreateServiceImpl implements DeliveryCreateService {

    private final RouteLogServicePort routeLogServicePort;

    private final DeliveryCreateRepository deliveryCreateRepository;

    @Override
    public Response createDelivery(Request request) {
        //final TerminalResponse response = routeLogServicePort.logRoute(request);
        final DeliveryCreate deliveryCreate = deliveryCreateRepository.save(request);
		return new Response(deliveryCreate.getTerminalId(), deliveryCreate.getVersion(),
				deliveryCreate.getSupplierCode(), deliveryCreate.getParcelId(),
				UUID.fromString(deliveryCreate.getProcessId()));
    }
}
