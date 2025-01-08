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
import com.warehouse.routelogger.dto.DeliveryRequestDto;
import com.warehouse.routelogger.dto.DepotCodeRequestDto;
import com.warehouse.routelogger.dto.ProcessTypeDto;
import com.warehouse.routelogger.event.DeliveryLogEvent;
import com.warehouse.routelogger.event.DepartmentCodeLogEvent;
import com.warehouse.routelogger.event.SupplierCodeLogEvent;

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
    public void logSupplierCode(final DeliveryReturn deliveryReturn) {
        sendEvent(buildSupplierCodeLogEvent(deliveryReturn));
    }

    private List<DepartmentCodeLogEvent> buildDepotCodeLogEvents(final DeliveryReturnRequest deliveryReturnRequest) {
        final List<DepartmentCodeLogEvent> departmentCodeLogEvents = new ArrayList<>();
        deliveryReturnRequest.getDeliveryReturnDetails().forEach(
                deliveryReturnDetails -> {
                    final DepotCodeRequestDto depotCodeRequest = DepotCodeRequestDto
                            .builder()
                            .depotCode(deliveryReturnRequest.getDepartmentCode())
                            .parcelId(deliveryReturnDetails.getShipmentId().getValue())
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

    private void sendEvent(final RouteLogEvent event) {
        routeLogEventPublisher.send(event);
    }
}
