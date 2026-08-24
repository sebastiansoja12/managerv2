package com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;

import java.util.Optional;

public class OperatorConfigurationPortImpl implements OperatorConfigurationPort {

    private final OperatorConfigurationService operatorConfigurationService;
    private final CurrentOperatorService currentOperatorService;

    public OperatorConfigurationPortImpl(final OperatorConfigurationService operatorConfigurationService,
                                         final CurrentOperatorService currentOperatorService) {
        this.operatorConfigurationService = operatorConfigurationService;
        this.currentOperatorService = currentOperatorService;
    }

    @Override
    public Optional<OperatorConfiguration> getCurrent() {
        final OperatorId operatorId = currentOperatorService.getCurrentOperatorId();
        return operatorConfigurationService.getByOperatorId(operatorId);
    }

    @Override
    public OperatorConfiguration updateCurrentShipmentConfiguration(final ShipmentConfiguration shipmentConfiguration) {
        final OperatorId operatorId = currentOperatorService.getCurrentOperatorId();
        final OperatorConfiguration currentConfiguration = operatorConfigurationService.getByOperatorId(operatorId)
                .orElseGet(() -> OperatorConfiguration.defaultFor(false, false, false));
        return operatorConfigurationService.create(operatorId, new OperatorConfiguration(
                currentConfiguration.getShippingCapabilities(),
                shipmentConfiguration,
                currentConfiguration.getDeliveryTimeConfiguration()
        ));
    }

    @Override
    public void create(final OperatorId operatorId, final OperatorConfiguration configuration) {
        operatorConfigurationService.create(operatorId, configuration);
    }
}
