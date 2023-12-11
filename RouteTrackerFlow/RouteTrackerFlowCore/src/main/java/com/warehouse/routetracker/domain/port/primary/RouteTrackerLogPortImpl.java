package com.warehouse.routetracker.domain.port.primary;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.model.*;
import org.apache.commons.lang3.StringUtils;

import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.domain.vo.*;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@AllArgsConstructor
public class RouteTrackerLogPortImpl implements RouteTrackerLogPort {

    private final RouteRepository repository;

    private final Logger logger = LoggerFactory.getLogger(RouteTrackerLogPort.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initializeRoute(Long parcelId) {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .parcelId(parcelId)
                .build();
        repository.save(routeLogRecord);
    }

    @Override
    public void saveDelivery(List<DeliveryInformation> deliveryInformation) {
		deliveryInformation.stream().filter(this::existsToken).forEach(request -> {
			final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                    .supplierCode(request.getSupplierCode())
					.parcelId(request.getParcelId())
                    .depotCode(request.getDepotCode())
                    .parcelStatus(request.getDeliveryParcelStatus())
                    .build();
            repository.save(routeLogRecord);
		});
    }

    @Override
    public List<RouteResponse> saveRoutes(List<RouteRequest> routeRequests) {
        logRouteRequest(routeRequests);
		return routeRequests.stream()
                .map(this::mapToRoute)
                .peek(this::logRouteRecord)
                .map(repository::save)
				.collect(Collectors.toList());
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        repository.deleteRoute(request);
    }

    @Override
    public RouteProcess initializeRouteProcess(ParcelId parcelId) {
        final RouteLogRecordToChange routeLogRecordToChange = RouteLogRecordToChange
                .builder()
                .parcelId(parcelId.getValue())
                .build();
        return repository.save(routeLogRecordToChange);
    }

    @Override
    public List<RouteInformation> getRouteListByParcelId(Long parcelId) {
        return repository.findByParcelId(parcelId);
    }

    @Override
    public List<RouteInformation> findRoutesByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public void saveZebraIdInformation(ZebraIdInformation information) {
        final RouteLogRecordToChange routeLogRecord = repository.find(information.getParcelId());
        routeLogRecord.saveZebraIdInformation(information.getProcessType(), information.getZebraId());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveZebraVersionInformation(ZebraVersionInformation information) {
        final RouteLogRecordToChange routeLogRecord = repository.find(information.getParcelId());
        routeLogRecord.saveZebraVersionInformation(information.getProcessType(), information.getVersion());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnErrorCode(ErrorInformation information) {
        final RouteLogRecordToChange routeLogRecord = repository.find(information.getParcelId());
        routeLogRecord.saveErrorReturnCode(information.getError());
        repository.update(routeLogRecord);
    }

    @Override
    public void saveFaultDescription(ProcessType processType, Long parcelId, String faultDescription) {
        final RouteLogRecordToChange routeLogRecord = repository.find(parcelId);
        routeLogRecord.saveFaultDescription(faultDescription);
        repository.update(routeLogRecord);
    }

    @Override
    public void saveTerminalRequest(TerminalRequest request) {
        final RouteLogRecordToChange routeLogRecord = repository.find(request.getParcelId());
        try {
            routeLogRecord.saveTerminalRequest(request.getProcessType(), mapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        repository.update(routeLogRecord);
    }

    @Override
    public void saveReturnTrackRequest(ReturnTrackRequest request) {
        final RouteLogRecordToChange routeLogRecord = repository.find(request.getParcelId());
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
    public RouteLogRecordToChange find(Long parcelId) {
        return repository.find(parcelId);
    }

    private void logRouteRecord(RouteLogRecord routeLogRecord) {
        logger.info("Saving route for parcel: {}", routeLogRecord.getParcelId());
    }

    private void logRouteRequest(List<RouteRequest> routeRequests) {
        logger.info("Detected request route for parcels: {}", routeRequests
                .stream()
                .map(RouteRequest::getParcelId)
                .collect(Collectors.toList()));
    }

    private RouteLogRecord mapToRoute(RouteRequest routeRequest) {
        return RouteLogRecord.builder()
                .parcelId(routeRequest.getParcelId())
                .supplierCode(routeRequest.getSupplierCode())
                .depotCode(routeRequest.getDepotCode())
                .username(routeRequest.getUsername())
                .build();
    }

    private boolean existsToken(DeliveryInformation deliveryInformation) {
        return Objects.nonNull(deliveryInformation) && StringUtils.isNotEmpty(deliveryInformation.getToken());
    }
}
