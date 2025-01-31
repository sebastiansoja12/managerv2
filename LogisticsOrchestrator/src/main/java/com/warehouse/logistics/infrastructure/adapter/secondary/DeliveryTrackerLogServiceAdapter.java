package com.warehouse.logistics.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Set;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.port.secondary.DeliveryTrackerLogServicePort;
import com.warehouse.logistics.domain.vo.DepartmentCodeRequest;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryEventMapper;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.dto.ProcessTypeDto;
import com.warehouse.routelogger.dto.ShipmentIdDto;
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

    @Override
    public void logDeviceInformation(final Set<ShipmentId> shipmentIds,
                                     final DeviceInformation deviceInformation, final com.warehouse.terminal.enumeration.ProcessType processType) {
        shipmentIds.forEach(shipmentId ->
                sendEvent(buildDeviceInformationLogEvent(ProcessType.valueOf(processType.name()), shipmentId, deviceInformation)));
    }

    private DeviceInformationLogEvent buildDeviceInformationLogEvent(final ProcessType processType,
                                                                     final ShipmentId shipmentId,
                                                                     final DeviceInformation deviceInformation) {
        return DeviceInformationLogEvent.builder()
                .device(eventMapper.map(deviceInformation))
                .processType(ProcessTypeDto.valueOf(processType.name()))
                .shipmentId(new ShipmentIdDto(shipmentId.getValue()))
                .build();
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
