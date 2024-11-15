package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.terminal.domain.model.request.DevicePairRequest;
import com.warehouse.terminal.request.DevicePairRequestDto;

@Mapper
public interface TerminalRequestMapper {

    @Mapping(target = "deviceId.value", source = "terminalId.value")
    DevicePairRequest map(final DevicePairRequestDto terminalPairRequest);
}
