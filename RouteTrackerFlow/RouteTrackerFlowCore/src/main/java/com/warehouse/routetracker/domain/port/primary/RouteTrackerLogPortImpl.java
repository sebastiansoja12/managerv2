package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.domain.model.FaultDescription;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.domain.service.JsonToStringService;
import com.warehouse.routetracker.domain.service.JsonToStringServiceImpl;
import com.warehouse.routetracker.domain.vo.*;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final JsonToStringService jsonToStringService = new JsonToStringServiceImpl();

    private final RouteLogRepository repository;

    @Override
    public RouteProcess initializeRouteProcess(final ShipmentId shipmentId) {
        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .parcelId(shipmentId.getValue())
                .build();
        return this.repository.save(routeLogRecord);
    }

    @Override
    public void saveDeviceIdInformation(final TerminalIdInformation information) {
        final RouteLogRecord routeLogRecord = this.repository.find(information.getShipmentId());
        routeLogRecord.saveTerminalId(information.getProcessType(), information.getTerminalId());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveDeviceVersionInformation(final TerminalVersionInformation information) {
        final RouteLogRecord routeLogRecord = this.repository.find(information.getShipmentId());
        routeLogRecord.saveDeviceVersion(information.getProcessType(), information.getVersion());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnErrorCode(final ErrorInformation information) {
        final RouteLogRecord routeLogRecord = this.repository.find(information.getShipmentId());
        routeLogRecord.saveErrorReturnCode(information.getError());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveFaultDescription(final FaultDescription faultDescription) {
        final RouteLogRecord routeLogRecord = this.repository.find(faultDescription.getShipmentId());
        routeLogRecord.saveFaultDescription(faultDescription.getDescription());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveDescription(final DescriptionRequest request) {
        final RouteLogRecord routeLogRecord = this.repository.find(request.getShipmentId());
        routeLogRecord.saveDescription(request.getProcessType(), request.getValue());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveSupplierCode(final SupplierCodeRequest request) {
        final RouteLogRecord routeLogRecord = this.repository.find(request.getShipmentId());
        routeLogRecord.saveSupplierCode(request.getProcessType(), request.getSupplierCode());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveUsername(final UsernameRequest request) {
        final RouteLogRecord routeLogRecord = this.repository.find(request.getShipmentId());
        routeLogRecord.saveUsername(request.getProcessType(), request.getUsername());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveDepotCode(final DepotCodeRequest request) {
        final RouteLogRecord routeLogRecord = this.repository.find(request.getShipmentId());
        routeLogRecord.saveDepotCode(request.getProcessType(), request.getDepotCode());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveTerminalRequest(final TerminalRequest terminalRequest) {
        final RouteLogRecord routeLogRecord = this.repository.find(terminalRequest.getShipmentId());
        routeLogRecord.updateRequest(terminalRequest.getProcessType(), terminalRequest.getRequestAsJson());
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnTrackRequest(final ReturnTrackRequest request) {
        final RouteLogRecord routeLogRecord = this.repository.find(request.getShipmentId());
        routeLogRecord.updateRequest(request.getProcessType(), this.jsonToStringService.convertToString(request));
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveDeliveryReturnRequest(final DeliveryReturnRequest request) {
        final RouteLogRecord routeLogRecord = this.repository.find(request.getShipmentId());
        routeLogRecord.updateRequest(request.getProcessType(), this.jsonToStringService.convertToString(request));
        this.repository.update(routeLogRecord);
    }

    @Override
    public void saveDeliveryStatus(final DeliveryStatusRequest request) {
        final RouteLogRecord routeLogRecord = this.repository.find(request.getShipmentId());
        routeLogRecord.updateShipmentStatus(request.getProcessType(), determineParcelStatus(request));
        this.repository.update(routeLogRecord);
    }

    @Override
    public RouteLogRecord find(final ShipmentId shipmentId) {
        return this.repository.find(shipmentId);
    }

    @Override
    public List<RouteLogRecord> findAll() {
        return this.repository.findAll();
    }

    private ParcelStatus determineParcelStatus(DeliveryStatusRequest request) {
        final ProcessType processType = request.getProcessType();
        return switch (processType) {
            case CREATED -> ParcelStatus.CREATED;
            case ROUTE, MISS -> ParcelStatus.DELIVERY;
            case RETURN -> ParcelStatus.RETURN;
            case REROUTE -> ParcelStatus.REROUTE;
            case REDIRECT, REJECT -> ParcelStatus.REDIRECT;
        };
    }
}
