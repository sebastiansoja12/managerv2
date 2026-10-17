package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.OperatorDevice;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

import java.util.List;

public interface OperatorDeviceServicePort {

    List<OperatorDevice> findAll(final OperatorScope operatorScope);

    boolean revoke(final OperatorScope operatorScope, final String deviceId);

    void changeStatus(final OperatorScope operatorScope, final String deviceId, final String status);

    int retireAll(final OperatorScope operatorScope);
}
