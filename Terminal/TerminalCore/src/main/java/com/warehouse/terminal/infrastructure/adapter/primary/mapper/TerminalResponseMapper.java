package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.vo.DeviceInformationResponse;
import com.warehouse.terminal.domain.vo.DevicePairResponse;
import com.warehouse.terminal.dto.*;
import com.warehouse.terminal.response.DeviceInformationResponseDto;
import com.warehouse.terminal.response.DevicePairResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface TerminalResponseMapper {

    default DeviceDto mapToDeviceResponse(final Terminal terminal) {
        return null;
    }

    DeviceTypeDto map(final DeviceType deviceType);

    DeviceInformationResponseDto map(final DeviceInformationResponse deviceInformationResponse);

    DevicePairResponseDto map(final DevicePairResponse devicePairResponse);
}
