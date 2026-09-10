package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import org.mapstruct.Mapper;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.SupplierId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.*;

@Mapper
public interface RouteResponseMapper {

    default RouteProcessDto map(final RouteProcess routeProcess) {
        final ShipmentIdDto shipmentId = new ShipmentIdDto(routeProcess.getShipmentId().value());
        return new RouteProcessDto(shipmentId, routeProcess.getProcessId());
    }

    default RouteLogRecordDto map(final RouteLogRecord routeLogRecord) {
        final ProcessIdDto processId = new ProcessIdDto(routeLogRecord.getId());
        final ShipmentIdDto shipmentId = new ShipmentIdDto(routeLogRecord.getShipmentId().value());
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
                        map(routeLogRecordDetail.getUserId()),
                        map(routeLogRecordDetail.getSupplierId()),
                        map(routeLogRecordDetail.getDepartmentId()),
                        map(routeLogRecordDetail.getShipmentStatus()),
                        routeLogRecordDetail.getDescription(),
                        routeLogRecordDetail.getTimestamp(),
                        map(routeLogRecordDetail.getProcessType()),
                        routeLogRecordDetail.getRequest()
                );
    }

    ShipmentStatusDto map(final ShipmentStatus shipmentStatus);

    default UserIdDto map(final UserId userId) {
        return userId != null ? new UserIdDto(userId.value()) : null;
    }

    default SupplierIdDto map(final SupplierId supplierId) {
        return supplierId != null ? new SupplierIdDto(supplierId.value()) : null;
    }

    default DepartmentIdDto map(final DepartmentId departmentId) {
        return departmentId != null ? new DepartmentIdDto(departmentId.value()) : null;
    }

    ProcessTypeDto map(final ProcessType processType);

    List<RouteLogRecordDto> mapToLogRecord(final List<RouteLogRecord> routeLogRecords);
}
