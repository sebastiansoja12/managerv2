package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.vo.ParcelId;
import com.warehouse.routetracker.infrastructure.api.dto.*;
import org.mapstruct.Mapper;

@Mapper
public interface RouteRequestMapper {

    RouteRequestDto map(RouteRequest routeRequest);

    List<RouteRequest> map(List<RouteRequestDto> routeRequest);

    RouteRequest map(RouteRequestDto routeRequestDto);

    ParcelId map(ParcelIdDto id);

    ZebraIdInformation map(ZebraIdInformationDto zebraIdInformation);

    ZebraVersionInformation map(ZebraVersionInformationDto versionInformation);

    ErrorInformation map(ErrorInformationDto errorInformation);

    TerminalRequest map(TerminalRequestDto terminalRequest);

    ReturnTrackRequest map(ReturnTrackRequestDto returnTrackRequest);
}
