package com.warehouse.terminal;

import com.warehouse.terminal.response.DeviceAuthenticationResponseDto;
import com.warehouse.terminal.response.DeviceInformationResponseDto;

public interface DeviceApiService {
    DeviceInformationResponseDto isDeviceValidByPairKey(final String pairKey);
    DeviceAuthenticationResponseDto authenticateByPairKey(final String pairKey);
}
