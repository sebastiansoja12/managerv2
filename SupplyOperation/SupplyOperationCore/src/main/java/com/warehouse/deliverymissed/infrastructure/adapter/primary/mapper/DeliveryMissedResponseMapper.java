package com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;
import com.warehouse.terminal.response.TerminalResponse;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryMissedResponseMapper {
    TerminalResponse map(DeliveryMissedResponse response);
}
