package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.domain.vo.*;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteLogRepository repository;

    private final Logger logger = LoggerFactory.getLogger(RouteTrackerLogPort.class);

    private final ObjectMapper mapper = new ObjectMapper();

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
    public void saveTerminalRequest(TerminalRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        try {
            routeLogRecord.saveTerminalRequest(request.getProcessType(), mapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnTrackRequest(ReturnTrackRequest request) {
        final RouteLogRecord routeLogRecord = repository.find(request.getParcelId());
        try {
            routeLogRecord.saveReturnTrackRequest(request.getProcessType(), mapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        routeLogRecord.saveUsername(request.getProcessType(), request.getUsername());
        routeLogRecord.saveDepotCode(request.getProcessType(), request.getDepotCode());
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
}
