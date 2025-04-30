package com.warehouse.routelogger.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routelogger.domain.model.*;
import com.warehouse.routelogger.dto.*;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {

    AnyDeliveryRequest map(DeliveryRequestDto request);

    default ShipmentId map(final ShipmentIdDto shipmentId) {
        return new ShipmentId(shipmentId.value());
    }

    DepotCodeRequest mapFromDepotCodeRequest(DepotCodeRequestDto depotCodeRequest);

    Request mapToRequest(RequestDto request);

    SupplierCodeRequest mapToSupplierCodeRequest(SupplierCodeRequestDto supplierCodeRequest);

    TerminalLogRequest mapToTerminalLogRequest(TerminalLogRequestDto terminalLogRequest);

    VersionLogRequest mapToVersionLogRequest(VersionLogRequestDto versionLogRequest);

    UsernameLogRequest mapToUsernameLogRequest(UsernameLogRequestDto usernameLogRequest);
}
