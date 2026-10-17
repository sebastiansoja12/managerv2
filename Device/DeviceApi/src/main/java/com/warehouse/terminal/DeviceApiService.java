package com.warehouse.terminal;

import java.util.List;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.DeviceAccessDto;
import com.warehouse.terminal.response.DeviceAuthenticationResponseDto;
import com.warehouse.terminal.response.DeviceInformationResponseDto;

public interface DeviceApiService {
    DeviceInformationResponseDto isDeviceValidByPairKey(final String pairKey);
    DeviceAuthenticationResponseDto authenticateByPairKey(final String pairKey);

    List<DeviceAccessDto> findAllAccess();

    boolean revokeAccess(final DeviceIdDto deviceId);

    void changeStatus(final DeviceIdDto deviceId, final String status);

    int retireAll();
}
