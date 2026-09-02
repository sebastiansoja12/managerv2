package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;

import java.util.Optional;

public interface OperatorShipmentSettingsServicePort {

    Optional<OperatorShipmentSettings> find(final OperatorScope operatorScope);

    OperatorShipmentSettings update(final OperatorScope operatorScope, final OperatorShipmentSettings settings);
}
