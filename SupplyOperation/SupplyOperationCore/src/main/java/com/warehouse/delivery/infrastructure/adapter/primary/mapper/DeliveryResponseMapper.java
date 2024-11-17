package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.vo.Response;
import com.warehouse.delivery.infrastructure.adapter.primary.dto.DeliveryResponseDto;
import com.warehouse.terminal.model.DeliveryRejectResponse;
import com.warehouse.terminal.response.TerminalResponse;

public interface DeliveryResponseMapper {
    TerminalResponse map(final Response response);

    DeliveryRejectResponse map(final com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse response);

    List<DeliveryResponseDto> map(List<DeliveryResponse> responses);
}
