package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.domain.vo.OperatorApiKey;
import com.warehouse.platformadministration.domain.vo.OperatorDevice;

import java.util.List;

public interface OperatorDevicePort {

    List<OperatorDevice> findDevices(final OperatorId operatorId);

    void revokeDevice(final OperatorId operatorId, final String deviceId);

    void changeDeviceStatus(final OperatorId operatorId, final String deviceId, final String status);

    List<OperatorApiKey> findApiKeys(final OperatorId operatorId);

    void revokeApiKey(final OperatorId operatorId, final UserId userId);
}
