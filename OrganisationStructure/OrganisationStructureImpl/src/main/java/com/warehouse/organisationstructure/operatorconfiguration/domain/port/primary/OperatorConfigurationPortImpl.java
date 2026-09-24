package com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentValidationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLabelConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLimits;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentWorkflowConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberRule;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentNotificationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;

import java.util.Optional;
import java.util.function.UnaryOperator;

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
        return updateCurrentShipmentConfiguration(currentShipmentConfiguration -> shipmentConfiguration);
    }

    @Override
    public ShipmentConfiguration updateCurrentShipmentValidationConfiguration(final ShipmentValidationConfiguration validationConfiguration) {
        return updateCurrentShipmentConfiguration(
                shipmentConfiguration -> shipmentConfiguration.withValidationConfiguration(validationConfiguration))
                .getShipmentConfiguration();
    }

    @Override
    public ShipmentConfiguration updateCurrentShipmentLabelConfiguration(final ShipmentLabelConfiguration labelConfiguration) {
        return updateCurrentShipmentConfiguration(
                shipmentConfiguration -> shipmentConfiguration.withLabelConfiguration(labelConfiguration))
                .getShipmentConfiguration();
    }

    @Override
    public ShipmentConfiguration updateCurrentShipmentLimits(final ShipmentLimits shipmentLimits) {
        return updateCurrentShipmentConfiguration(
                shipmentConfiguration -> shipmentConfiguration.withShipmentLimits(shipmentLimits))
                .getShipmentConfiguration();
    }

    @Override
    public ShipmentConfiguration updateCurrentShipmentWorkflowConfiguration(final ShipmentWorkflowConfiguration workflowConfiguration) {
        return updateCurrentShipmentConfiguration(
                shipmentConfiguration -> shipmentConfiguration.withWorkflowConfiguration(workflowConfiguration))
                .getShipmentConfiguration();
    }

    @Override
    public ShipmentConfiguration updateCurrentTrackingNumberRule(final TrackingNumberRule trackingNumberRule) {
        return updateCurrentShipmentConfiguration(
                shipmentConfiguration -> shipmentConfiguration.withTrackingNumberRule(trackingNumberRule))
                .getShipmentConfiguration();
    }

    @Override
    public ShipmentConfiguration updateCurrentShipmentNotificationConfiguration(final ShipmentNotificationConfiguration notificationConfiguration) {
        return updateCurrentShipmentConfiguration(
                shipmentConfiguration -> shipmentConfiguration.withNotificationConfiguration(notificationConfiguration))
                .getShipmentConfiguration();
    }

    @Override
    public DepartmentConfiguration getCurrentDepartmentConfiguration() {
        return getCurrent()
                .map(OperatorConfiguration::getDepartmentConfiguration)
                .orElseGet(DepartmentConfiguration::defaultConfiguration);
    }

    @Override
    public DepartmentConfiguration updateCurrentDepartmentConfiguration(
            final DepartmentConfiguration departmentConfiguration) {
        final OperatorId operatorId = currentOperatorService.getCurrentOperatorId();
        final OperatorConfiguration currentConfiguration = operatorConfigurationService.getByOperatorId(operatorId)
                .orElseGet(() -> OperatorConfiguration.defaultFor(false, false, false));
        return operatorConfigurationService.create(operatorId,
                currentConfiguration.withDepartmentConfiguration(departmentConfiguration))
                .getDepartmentConfiguration();
    }

    @Override
    public void create(final OperatorId operatorId, final OperatorConfiguration configuration) {
        operatorConfigurationService.create(operatorId, configuration);
    }

    @Override
    public ShipmentNotificationConfiguration getShipmentNotificationConfiguration(final OperatorId operatorId) {
        return operatorConfigurationService.getByOperatorId(operatorId)
                .map(OperatorConfiguration::getShipmentConfiguration)
                .map(ShipmentConfiguration::getNotificationConfiguration)
                .orElseGet(ShipmentNotificationConfiguration::defaultConfiguration);
    }

    private OperatorConfiguration updateCurrentShipmentConfiguration(
            final UnaryOperator<ShipmentConfiguration> shipmentConfigurationChange) {
        final OperatorId operatorId = currentOperatorService.getCurrentOperatorId();
        final OperatorConfiguration currentConfiguration = operatorConfigurationService.getByOperatorId(operatorId)
                .orElseGet(() -> OperatorConfiguration.defaultFor(false, false, false));
        final ShipmentConfiguration currentShipmentConfiguration = Optional
                .ofNullable(currentConfiguration.getShipmentConfiguration())
                .orElseGet(ShipmentConfiguration::defaultConfiguration);
        return operatorConfigurationService.create(operatorId, currentConfiguration.withShipmentConfiguration(
                shipmentConfigurationChange.apply(currentShipmentConfiguration)));
    }
}
