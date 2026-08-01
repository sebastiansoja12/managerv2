package com.warehouse.routetracker.domain.port.primary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.DeviceInformationRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.domain.vo.DepotCodeRequest;
import com.warehouse.routetracker.domain.vo.DeviceIdInformation;
import com.warehouse.routetracker.domain.vo.DeviceVersionInformation;
import com.warehouse.routetracker.domain.vo.DeliveryStatusRequest;
import com.warehouse.routetracker.domain.vo.SupplierCodeRequest;
import com.warehouse.routetracker.domain.vo.TerminalRequest;
import com.warehouse.routetracker.domain.vo.UsernameRequest;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteLogRepository repository;

    public RouteTrackerLogPortImpl(final RouteLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveShipmentEvent(final ShipmentId shipmentId,
                                  final String eventType,
                                  final ParcelStatus parcelStatus,
                                  final LocalDateTime occurredAt,
                                  final String payload) {
        this.update(shipmentId,
                routeLogRecord -> routeLogRecord.recordShipmentEvent(eventType, parcelStatus, occurredAt, payload));
    }

    @Override
    public void saveDeliveryStatus(final DeliveryStatusRequest request) {
        this.update(request.getShipmentId(), routeLogRecord -> routeLogRecord.updateShipmentStatus(
                request.getProcessType(), this.determineParcelStatus(request.getProcessType())));
    }

    @Override
    public void saveDepotCode(final DepotCodeRequest request) {
        this.update(request.getShipmentId(), routeLogRecord ->
                routeLogRecord.saveDepotCode(request.getProcessType(), request.getDepotCode()));
    }

    @Override
    public void saveSupplierCode(final SupplierCodeRequest request) {
        this.update(request.getShipmentId(), routeLogRecord ->
                routeLogRecord.saveSupplierCode(request.getProcessType(), request.getSupplierCode()));
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
    public void saveUsername(final UsernameRequest request) {
        this.update(request.getShipmentId(), routeLogRecord ->
                routeLogRecord.saveUsername(request.getProcessType(), request.getUsername()));
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
        this.repository.findOptional(shipmentId).ifPresentOrElse(routeLogRecord -> {
            change.accept(routeLogRecord);
            this.repository.update(routeLogRecord);
        }, () -> {
            final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                    .parcelId(shipmentId.value())
                    .build();
            change.accept(routeLogRecord);
            this.repository.save(routeLogRecord);
        });
    }

    private ParcelStatus determineParcelStatus(final ProcessType processType) {
        return switch (processType) {
            case CREATED -> ParcelStatus.CREATED;
            case ROUTE, MISS -> ParcelStatus.DELIVERY;
            case RETURN -> ParcelStatus.RETURN;
            case REROUTE -> ParcelStatus.REROUTE;
            case REDIRECT, REJECT -> ParcelStatus.REDIRECT;
        };
    }
}
