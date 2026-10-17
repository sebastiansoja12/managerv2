package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;

public interface OperatorSettingsPort {

    OperatorShipmentSettings findShipmentSettings(final OperatorId operatorId);

    OperatorShipmentSettings updateShipmentSettings(final OperatorId operatorId, final OperatorShipmentSettings settings);
}
