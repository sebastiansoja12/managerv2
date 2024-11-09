package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
import com.warehouse.routetracker.domain.vo.RouteInformation;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.*;

@Mapper
public interface RouteResponseMapper {

    List<RouteInformationDto> map(final List<RouteInformation> routes);

    @Mapping(target = "status", source = "parcelStatus")
    RouteInformationDto map(final RouteInformation routeInformation);

    RouteProcessDto map(final RouteProcess routeProcess);

    default RouteLogRecordDto map(final RouteLogRecord routeLogRecord) {
        final ProcessIdDto processId = new ProcessIdDto(routeLogRecord.getId());
        final ShipmentIdDto shipmentId = new ShipmentIdDto(routeLogRecord.getParcelId());
        final RouteLogRecordDetailsDto routeLogRecordDetailsDto = map(routeLogRecord.getRouteLogRecordDetails());
        final ReturnCodeDto returnCode = new ReturnCodeDto(routeLogRecord.getReturnCode());
        final FaultDescriptionDto faultDescription = new FaultDescriptionDto(routeLogRecord.getFaultDescription());
        return new RouteLogRecordDto(processId, shipmentId, routeLogRecordDetailsDto, returnCode, faultDescription);
    }

    RouteLogRecordDetailsDto map(final RouteLogRecordDetails routeLogRecordDetails);

    default RouteLogRecordDetailDto map(final RouteLogRecordDetail routeLogRecordDetail) {
        return new RouteLogRecordDetailDto(routeLogRecordDetail.getId(),
                TerminalIdDto.from(routeLogRecordDetail.getTerminalId()),
                        routeLogRecordDetail.getVersion(),
                        routeLogRecordDetail.getUsername(),
                        routeLogRecordDetail.getSupplierCode(),
                        routeLogRecordDetail.getDepotCode(),
                        map(routeLogRecordDetail.getParcelStatus()),
                        routeLogRecordDetail.getDescription(),
                        routeLogRecordDetail.getTimestamp(),
                        map(routeLogRecordDetail.getProcessType()),
                        routeLogRecordDetail.getRequest()
                );
    }

    ShipmentStatusDto map(final ParcelStatus parcelStatus);

    ProcessTypeDto map(final ProcessType parcelType);

    List<RouteLogRecordDto> mapToLogRecord(final List<RouteLogRecord> routeLogRecords);
}
