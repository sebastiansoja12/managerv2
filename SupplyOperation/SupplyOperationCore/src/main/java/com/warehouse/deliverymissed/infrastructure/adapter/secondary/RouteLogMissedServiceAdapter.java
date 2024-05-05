package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper.DeliveryMissedEventMapper;
import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.event.*;
import com.warehouse.terminal.request.TerminalRequest;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;


@AllArgsConstructor
public class RouteLogMissedServiceAdapter implements RouteLogMissedServicePort {


    private final RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryMissedEventMapper eventMapper = getMapper(DeliveryMissedEventMapper.class);


    @Override
    public void logRouteLogMissedDelivery(final DeliveryMissed deliveryMissed) {
        sendEvent(buildDeliveryLogEvent(deliveryMissed));
    }

    @Override
    public void logDepotCodeMissedDelivery(final DeliveryMissed deliveryMissed) {
        sendEvent(buildDepotCodeLogEvent(deliveryMissed));
    }

    @Override
    public void logRequest(final TerminalRequest terminalRequest, final String requestAsJson) {
        sendEvent(buildRequestLogEvent(terminalRequest, requestAsJson));
    }

    @Override
    public void logSupplierCode(DeliveryMissed deliveryMissed) {
        sendEvent(buildSupplierCodeLogEvent(deliveryMissed));
    }

    @Override
    public void logTerminalId(TerminalRequest terminalRequest) {
        sendEvent(buildTerminalLogEvent(terminalRequest));
    }

    private DepotCodeLogEvent buildDepotCodeLogEvent(final DeliveryMissed deliveryMissed) {
        return DepotCodeLogEvent.builder()
                .depotCodeRequest(eventMapper.mapToDepotCodeRequest(deliveryMissed))
                .build();
    }

    private DeliveryLogEvent buildDeliveryLogEvent(final DeliveryMissed deliveryMissed) {
        return DeliveryLogEvent.builder()
                .deliveryRequest(eventMapper.map(deliveryMissed))
                .build();
    }

    private SupplierCodeLogEvent buildSupplierCodeLogEvent(final DeliveryMissed deliveryMissed) {
        return SupplierCodeLogEvent.builder()
                .supplierCodeRequest(eventMapper.mapToSupplierCodeRequest(deliveryMissed))
                .build();
    }

	private RequestLogEvent buildRequestLogEvent(final TerminalRequest terminalRequest, final String requestAsJson) {
        return RequestLogEvent.builder()
                .request(eventMapper.map(terminalRequest, requestAsJson))
                .build();
    }

    private TerminalLogEvent buildTerminalLogEvent(final TerminalRequest terminalRequest) {
        return TerminalLogEvent.builder()
                .terminalLogRequest(eventMapper.mapToTerminalLogRequest(terminalRequest))
                .build();
    }

    private void sendEvent(final RouteLogEvent event) {
        routeLogEventPublisher.send(event);
    }
}
