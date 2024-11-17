package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.ArrayList;
import java.util.List;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogReturnServicePort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryReturnEventMapper;
import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.dto.*;
import com.warehouse.routelogger.event.*;
import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.request.TerminalRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLogReturnServiceAdapter implements RouteLogReturnServicePort {

    private final RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryReturnEventMapper eventMapper = getMapper(DeliveryReturnEventMapper.class);

    @Override
    public void logRouteLogReturnDelivery(final DeliveryReturnRouteRequest deliveryReturnRouteRequest) {
        buildDeliveryLogEvents(deliveryReturnRouteRequest).forEach(this::sendEvent);
    }

    @Override
    public void logDepotCodeReturnDelivery(final DeliveryReturnRequest deliveryReturnRequest) {
        buildDepotCodeLogEvents(deliveryReturnRequest).forEach(this::sendEvent);
    }

    @Override
    public void logRequest(final TerminalRequest terminalRequest, final String requestAsJson) {
        buildRequestLogEvent(terminalRequest, requestAsJson).forEach(this::sendEvent);
    }

    @Override
    public void logSupplierCode(final DeliveryReturn deliveryReturn) {
        sendEvent(buildSupplierCodeLogEvent(deliveryReturn));
    }

    @Override
    public void logTerminalId(final TerminalRequest terminalRequest) {
        buildTerminalLogEvents(terminalRequest).forEach(this::sendEvent);
    }

    @Override
    public void logVersion(final TerminalRequest terminalRequest) {
        buildVersionLogEvents(terminalRequest).forEach(this::sendEvent);
    }

    private List<DepartmentCodeLogEvent> buildDepotCodeLogEvents(final DeliveryReturnRequest deliveryReturnRequest) {
        final List<DepartmentCodeLogEvent> departmentCodeLogEvents = new ArrayList<>();
        deliveryReturnRequest.getDeliveryReturnDetails().forEach(
                deliveryReturnDetails -> {
                    final DepotCodeRequestDto depotCodeRequest = DepotCodeRequestDto
                            .builder()
                            .depotCode(deliveryReturnRequest.getDepotCode())
                            .parcelId(deliveryReturnDetails.getParcelId())
                            .processType(deliveryReturnRequest.getProcessType().name())
                            .build();
                    departmentCodeLogEvents.add(new DepartmentCodeLogEvent(depotCodeRequest));
                }
        );
        return departmentCodeLogEvents;
    }

    private List<DeliveryLogEvent> buildDeliveryLogEvents(final DeliveryReturnRouteRequest deliveryReturnRequest) {
        final List<DeliveryLogEvent> deliveryLogEvents = new ArrayList<>();
        deliveryReturnRequest.getDeliveryReturnRouteDetails().forEach(
                deliveryReturnDetails -> {
                    final DeliveryRequestDto request = DeliveryRequestDto.builder()
                            .depotCode(deliveryReturnRequest.getDepotCode())
                            .parcelId(deliveryReturnDetails.getParcelId())
                            .processType(ProcessTypeDto.RETURN)
                            .supplierCode(deliveryReturnRequest.getUsername())
                            .build();
                    deliveryLogEvents.add(new DeliveryLogEvent(request));
                }
        );
        return deliveryLogEvents;
    }

    private SupplierCodeLogEvent buildSupplierCodeLogEvent(final DeliveryReturn deliveryReturn) {
        return SupplierCodeLogEvent.builder()
                .supplierCodeRequest(eventMapper.mapToSupplierCodeRequest(deliveryReturn))
                .build();
    }

    private List<RequestLogEvent> buildRequestLogEvent(final TerminalRequest terminalRequest, final String requestAsJson) {
        final List<DeliveryReturnDetail> deliveryReturnDetails = terminalRequest.getDeliveryReturnDetails();
        final List<RequestLogEvent> requestLogEvents = new ArrayList<>();
        deliveryReturnDetails.forEach(
                deliveryReturnDetail -> {
                    final RequestDto request = RequestDto.builder()
                            .parcelId(deliveryReturnDetail.getShipmentId())
                            .request(requestAsJson)
                            .processType(ProcessTypeDto.RETURN)
                            .build();
                    requestLogEvents.add(new RequestLogEvent(request));
                }
        );
        return requestLogEvents;
    }

	private List<TerminalLogEvent> buildTerminalLogEvents(final TerminalRequest terminalRequest) {
		final List<DeliveryReturnDetail> deliveryReturnDetails = terminalRequest.getDeliveryReturnDetails();
		final List<TerminalLogEvent> terminalLogEvents = new ArrayList<>();
		deliveryReturnDetails.forEach(deliveryReturnDetail -> {
			final TerminalLogRequestDto request = new TerminalLogRequestDto(
					String.valueOf(terminalRequest.getDevice().getDeviceId()),
					ProcessTypeDto.RETURN, deliveryReturnDetail.getShipmentId());
			terminalLogEvents.add(new TerminalLogEvent(request));
		});
		return terminalLogEvents;
	}

    private List<VersionLogEvent> buildVersionLogEvents(final TerminalRequest terminalRequest) {
        final List<DeliveryReturnDetail> deliveryReturnDetails = terminalRequest.getDeliveryReturnDetails();
        final List<VersionLogEvent> terminalLogEvents = new ArrayList<>();
        deliveryReturnDetails.forEach(deliveryReturnDetail -> {
            final VersionLogRequestDto request = new VersionLogRequestDto(deliveryReturnDetail.getShipmentId(),
                    ProcessTypeDto.RETURN, terminalRequest.getDevice().getVersion());
            terminalLogEvents.add(new VersionLogEvent(request));
        });
        return terminalLogEvents;
    }

    private void sendEvent(final RouteLogEvent event) {
        routeLogEventPublisher.send(event);
    }
}
