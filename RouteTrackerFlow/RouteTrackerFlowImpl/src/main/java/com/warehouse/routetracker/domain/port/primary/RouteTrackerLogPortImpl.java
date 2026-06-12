package com.warehouse.routetracker.domain.port.primary;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.DeviceInformationRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteLogRepository repository;

    public RouteTrackerLogPortImpl(final RouteLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createShipmentEvent(final ShipmentId shipmentId,
                                    final String eventType,
                                    final ShipmentStatus shipmentStatus,
                                    final LocalDateTime occurredAt,
                                    final String payload,
                                    final UserId userId,
                                    final DepartmentId departmentId) {
        this.update(shipmentId, routeLogRecord -> {
            routeLogRecord.createShipmentEvent(
                    eventType, shipmentStatus, occurredAt, payload, userId, departmentId);
        });
    }

    @Override
    public void saveDeliveryStatus(final DeliveryStatusRequest request) {
        this.update(request.getShipmentId(), routeLogRecord -> routeLogRecord.updateShipmentStatus(
                request.getProcessType(), this.determineShipmentStatus(request.getProcessType())));
    }

    @Override
    public void saveDepartmentId(final DepartmentIdRequest request) {
        this.update(request.shipmentId(), routeLogRecord ->
                routeLogRecord.saveDepartmentId(request.processType(), request.departmentId()));
    }

    @Override
    public void saveSupplierId(final SupplierIdRequest request) {
        this.update(request.shipmentId(), routeLogRecord ->
                routeLogRecord.saveSupplierId(request.processType(), request.supplierId()));
    }

    @Override
    public void saveTerminalRequest(final TerminalRequest request) {
        this.update(request.getShipmentId(), routeLogRecord ->
                routeLogRecord.updateRequest(request.getProcessType(), request.getRequestAsJson()));
    }

    @Override
    public void saveDeviceIdInformation(final DeviceIdInformation information) {
        this.update(information.getShipmentId(), routeLogRecord ->
                routeLogRecord.saveTerminalId(information.getProcessType(), information.getTerminalId()));
    }

    @Override
    public void saveDeviceVersionInformation(final DeviceVersionInformation information) {
        this.update(information.getShipmentId(), routeLogRecord ->
                routeLogRecord.saveDeviceVersion(information.getProcessType(), information.getVersion()));
    }

    @Override
    public void saveUserId(final UserIdRequest request) {
        this.update(request.shipmentId(), routeLogRecord ->
                routeLogRecord.saveUserId(request.processType(), request.userId()));
    }

    @Override
    public void saveDeviceInformation(final DeviceInformationRequest request) {
        this.update(request.getShipmentId(), routeLogRecord -> routeLogRecord.updateDeviceInformation(request));
    }

    @Override
    public RouteLogRecord find(final ShipmentId shipmentId) {
        return this.repository.find(shipmentId);
    }

    @Override
    public List<RouteLogRecord> findAll() {
        return this.repository.findAll();
    }

    private void update(final ShipmentId shipmentId, final Consumer<RouteLogRecord> change) {
        this.repository.findById(shipmentId).ifPresentOrElse(routeLogRecord -> {
            change.accept(routeLogRecord);
            this.repository.update(routeLogRecord);
        }, () -> {
            final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                    .shipmentId(shipmentId)
                    .build();
            change.accept(routeLogRecord);
            this.repository.save(routeLogRecord);
        });
    }

    private ShipmentStatus determineShipmentStatus(final ProcessType processType) {
        return switch (processType) {
            case CREATED -> ShipmentStatus.CREATED;
            case ROUTE, MISS -> ShipmentStatus.DELIVERY;
            case RETURN -> ShipmentStatus.RETURN;
            case REROUTE -> ShipmentStatus.REROUTE;
            case REDIRECT, REJECT -> ShipmentStatus.REDIRECT;
        };
    }
}
