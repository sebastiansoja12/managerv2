package com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.terminal.request.TerminalRequest;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryMissedRequestMapper {
    DeliveryMissedRequest map(TerminalRequest terminalRequest);
}
