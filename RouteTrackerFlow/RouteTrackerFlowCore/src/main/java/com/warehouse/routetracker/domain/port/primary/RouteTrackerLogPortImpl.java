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
        return repository.save(routeLogRecord);
    }

    @Override
    public void saveTerminalIdInformation(final TerminalIdInformation information) {
        final RouteLogRecord routeLogRecord = repository.find(information.getShipmentId());
        routeLogRecord.saveTerminalId(information.getProcessType(), information.getTerminalId());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveZebraVersionInformation(final TerminalVersionInformation information) {
        final RouteLogRecord routeLogRecord = repository.find(information.getShipmentId());
        routeLogRecord.saveTerminalVersion(information.getProcessType(), information.getVersion());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnErrorCode(final ErrorInformation information) {
        final RouteLogRecord routeLogRecord = repository.find(information.getShipmentId());
        routeLogRecord.saveErrorReturnCode(information.getError());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveFaultDescription(final FaultDescription faultDescription) {
        final RouteLogRecord routeLogRecord = repository.find(faultDescription.getShipmentId());
        routeLogRecord.saveFaultDescription(faultDescription.getDescription());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveDescription(final DescriptionRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getShipmentId());
        routeLogRecord.saveDescription(request.getProcessType(), request.getValue());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveSupplierCode(final SupplierCodeRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getShipmentId());
        routeLogRecord.saveSupplierCode(request.getProcessType(), request.getSupplierCode());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveUsername(final UsernameRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getShipmentId());
        routeLogRecord.saveUsername(request.getProcessType(), request.getUsername());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveDepotCode(final DepotCodeRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getShipmentId());
        routeLogRecord.saveDepotCode(request.getProcessType(), request.getDepotCode());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveTerminalRequest(final TerminalRequest terminalRequest) {
        final RouteLogRecord routeLogRecord = repository.find(terminalRequest.getShipmentId());
        routeLogRecord.updateRequest(terminalRequest.getProcessType(), terminalRequest.getRequestAsJson());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnTrackRequest(final ReturnTrackRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getShipmentId());
        routeLogRecord.updateRequest(request.getProcessType(), jsonToStringService.convertToString(request));
        repository.update(routeLogRecord);
    }

    @Override
    public void saveDeliveryReturnRequest(final DeliveryReturnRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getShipmentId());
        routeLogRecord.updateRequest(request.getProcessType(), jsonToStringService.convertToString(request));
        repository.update(routeLogRecord);
    }

    @Override
    public void saveDeliveryStatus(final DeliveryStatusRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getShipmentId());
        routeLogRecord.updateShipmentStatus(request.getProcessType(), determineParcelStatus(request));
        repository.update(routeLogRecord);
    }

    @Override
    public RouteLogRecord find(final ShipmentId shipmentId) {
        return this.repository.find(shipmentId);
    }

    @Override
    public List<RouteLogRecord> findAll() {
        return repository.findAll();
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
