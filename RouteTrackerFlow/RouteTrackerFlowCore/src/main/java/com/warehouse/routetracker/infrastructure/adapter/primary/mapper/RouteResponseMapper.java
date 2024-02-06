package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.RouteInformationDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.RouteLogRecordDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.RouteProcessDto;

@Mapper
public interface RouteResponseMapper {

    List<RouteInformationDto> map(List<RouteInformation> routes);

    @Mapping(target = "status", source = "parcelStatus")
    RouteInformationDto map(RouteInformation routeInformation);

    RouteProcessDto map(RouteProcess routeProcess);

    @Mapping(target = "processId.value", source = "id")
    @Mapping(target = "parcelId.value", source = "parcelId")
    @Mapping(target = "returnCode.value", source = "returnCode")
    @Mapping(target = "faultDescription.value", source = "faultDescription")
    RouteLogRecordDto map(RouteLogRecord routeLogRecord);

    List<RouteLogRecordDto> mapToLogRecord(List<RouteLogRecord> routeLogRecords);
}
