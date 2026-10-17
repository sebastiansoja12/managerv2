package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentSettingsServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OperatorSettingsPortImpl implements OperatorSettingsPort {

    private final OperatorServicePort operatorServicePort;

    private final OperatorShipmentSettingsServicePort operatorShipmentSettingsServicePort;

    public OperatorSettingsPortImpl(final OperatorServicePort operatorServicePort,
                                    final OperatorShipmentSettingsServicePort operatorShipmentSettingsServicePort) {
        this.operatorServicePort = operatorServicePort;
        this.operatorShipmentSettingsServicePort = operatorShipmentSettingsServicePort;
    }

    @Override
    public OperatorShipmentSettings findShipmentSettings(final OperatorId operatorId) {
        return operatorShipmentSettingsServicePort.find(scope(operatorId))
                .orElseThrow(() -> new IllegalStateException("Operator " + operatorId.value() + " has no configuration"));
    }

    @Override
    public OperatorShipmentSettings updateShipmentSettings(final OperatorId operatorId,
                                                           final OperatorShipmentSettings settings) {
        if (settings == null || settings.values().isEmpty()) {
            throw new IllegalArgumentException("Shipment settings cannot be empty");
        }
        final OperatorShipmentSettings updatedSettings = operatorShipmentSettingsServicePort.update(scope(operatorId),
                settings);
        log.info("Platform owner changed shipment settings of operator {}", operatorId.value());
        return updatedSettings;
    }

    private OperatorScope scope(final OperatorId operatorId) {
        return operatorServicePort.findScope(operatorId)
                .orElseThrow(() -> new OperatorNotFoundException(operatorId));
    }
}
