package com.warehouse.delivery.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.delivery.domain.port.secondary.DeliveryTrackerLogServicePort;
import com.warehouse.delivery.domain.vo.DepartmentCodeRequest;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryEventMapper;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.event.*;
import com.warehouse.terminal.request.TerminalRequest;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DeliveryTrackerLogServiceAdapter implements DeliveryTrackerLogServicePort {

    private final RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryEventMapper eventMapper = getMapper(DeliveryEventMapper.class);

    @Override
    public void logDeliveryTracker(final DeliveryMissed deliveryMissed) {
        sendEvent(buildDeliveryLogEvent(deliveryMissed));
    }

    @Override
    public void logDepartmentCode(final DepartmentCodeRequest departmentCodeRequest) {
        sendEvent(buildDepartmentCodeEvent(departmentCodeRequest));
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
    public void logDeviceId(TerminalRequest terminalRequest) {
        sendEvent(buildTerminalLogEvent(terminalRequest));
    }

    @Override
    public void logVersion(TerminalRequest terminalRequest) {
        sendEvent(buildVersionLogEvent(terminalRequest));
    }

    private DepartmentCodeLogEvent buildDepartmentCodeEvent(final DepartmentCodeRequest departmentCodeRequest) {
        return DepartmentCodeLogEvent.builder()
                .depotCodeRequest(eventMapper.mapToDepotCodeRequest(departmentCodeRequest))
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

    private VersionLogEvent buildVersionLogEvent(final TerminalRequest terminalRequest) {
        return VersionLogEvent.builder()
                .versionLogRequest(eventMapper.mapToVersionLogRequest(terminalRequest))
                .build();
    }

    private void sendEvent(final RouteLogEvent event) {
        routeLogEventPublisher.send(event);
    }
}
