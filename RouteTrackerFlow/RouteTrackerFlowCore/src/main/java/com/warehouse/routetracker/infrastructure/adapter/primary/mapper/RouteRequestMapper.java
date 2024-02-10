package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.deliveryreturn.DeliveryReturnRequestDto;
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

    DeliveryReturnRequest map(DeliveryReturnRequestDto deliveryReturnRequest);

    SupplierCodeRequest map(SupplierCodeRequestDto supplierCodeRequest);

    DepotCodeRequest map(DepotCodeRequestDto depotCodeRequest);

    UsernameRequest map(UsernameRequestDto usernameRequest);

    DescriptionRequest map(DescriptionRequestDto descriptionRequest);

    ZebraInitializeRequest map(ZebraInitializeRequestDto initializeRequest);
}
