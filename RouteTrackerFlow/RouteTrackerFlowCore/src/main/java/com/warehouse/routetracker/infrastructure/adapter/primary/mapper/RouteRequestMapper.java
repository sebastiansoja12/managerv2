package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import com.warehouse.routetracker.infrastructure.adapter.primary.dto.*;
import org.mapstruct.Mapper;

import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.vo.ParcelId;

@Mapper
public interface RouteRequestMapper {

    ParcelId map(ParcelIdDto id);

    ZebraIdInformation map(ZebraIdInformationDto zebraIdInformation);

    ZebraVersionInformation map(ZebraVersionInformationDto versionInformation);

    ErrorInformation map(ErrorInformationDto errorInformation);

    TerminalRequest map(TerminalRequestDto terminalRequest);

    ReturnTrackRequest map(ReturnTrackRequestDto returnTrackRequest);
}
