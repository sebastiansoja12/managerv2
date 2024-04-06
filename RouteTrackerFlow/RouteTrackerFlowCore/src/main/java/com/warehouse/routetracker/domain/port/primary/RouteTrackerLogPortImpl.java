package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.domain.service.JsonToStringService;
import com.warehouse.routetracker.domain.service.JsonToStringServiceImpl;
import com.warehouse.routetracker.domain.vo.*;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteLogRepository repository;

    private final Logger logger = LoggerFactory.getLogger(RouteTrackerLogPort.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private final JsonToStringService jsonToStringService = new JsonToStringServiceImpl();

    @Override
    public RouteProcess initializeRouteProcess(ParcelId parcelId) {
        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .parcelId(parcelId.getValue())
                .build();
        return repository.save(routeLogRecord);
    }

    @Override
    public void saveZebraIdInformation(ZebraIdInformation information) {
        final RouteLogRecord routeLogRecord = repository.find(information.getParcelId());
        routeLogRecord.saveZebraIdInformation(information.getProcessType(), information.getZebraId());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveZebraVersionInformation(ZebraVersionInformation information) {
        final RouteLogRecord routeLogRecord = repository.find(information.getParcelId());
        routeLogRecord.saveZebraVersionInformation(information.getProcessType(), information.getVersion());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnErrorCode(ErrorInformation information) {
        final RouteLogRecord routeLogRecord = repository.find(information.getParcelId());
        routeLogRecord.saveErrorReturnCode(information.getError());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveFaultDescription(ProcessType processType, Long parcelId, String faultDescription) {
        final RouteLogRecord routeLogRecord = repository.find(parcelId);
        routeLogRecord.saveFaultDescription(faultDescription);
        repository.update(routeLogRecord);
    }

    @Override
    public void saveDescription(DescriptionRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        routeLogRecord.saveDescription(request.getProcessType(), request.getValue());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveSupplierCode(SupplierCodeRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        routeLogRecord.saveSupplierCode(request.getProcessType(), request.getSupplierCode());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveUsername(UsernameRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        routeLogRecord.saveUsername(request.getProcessType(), request.getUsername());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveDepotCode(DepotCodeRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        routeLogRecord.saveDepotCode(request.getProcessType(), request.getDepotCode());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveCreateRequest(ZebraInitializeRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        routeLogRecord.updateRequest(request.getProcessType(), jsonToStringService.convertToString(request));
        repository.update(routeLogRecord);
    }

    @Override
    public void saveTerminalRequest(TerminalRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        routeLogRecord.updateRequest(request.getProcessType(), jsonToStringService.convertToString(request));
        repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnTrackRequest(ReturnTrackRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        routeLogRecord.updateRequest(request.getProcessType(), jsonToStringService.convertToString(request));
        routeLogRecord.saveUsername(request.getProcessType(), request.getUsername());
        routeLogRecord.saveDepotCode(request.getProcessType(), request.getDepotCode());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveDeliveryReturnRequest(DeliveryReturnRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        final ProcessType processType = ProcessType.RETURN;
        routeLogRecord.updateRequest(processType, jsonToStringService.convertToString(request));
        routeLogRecord.saveDepotCode(processType, request.getDepotCode());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveDeliveryStatus(DeliveryStatusRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        routeLogRecord.saveParcelStatus(request.getProcessType(), determineParcelStatus(request));
        repository.update(routeLogRecord);
    }

    @Override
    public RouteLogRecord find(Long parcelId) {
        return repository.find(parcelId);
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
